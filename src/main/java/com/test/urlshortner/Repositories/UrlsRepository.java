package com.test.urlshortner.Repositories;

import com.google.zxing.WriterException;
import com.test.urlshortner.Models.CustomException;
import com.test.urlshortner.Models.ShortenUrlResponse;
import com.test.urlshortner.Models.Urls;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;
import redis.clients.jedis.UnifiedJedis;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.sql.Date;
import java.util.Arrays;
import java.util.Base64;
import java.util.Map;

@Repository
public class UrlsRepository implements IUrlRepository {

    private EntityManager em;
    private UnifiedJedis jedis;
    private QRService qr;

    @Autowired
    public void setJedis(UnifiedJedis jedis) {
        this.jedis = jedis;
    }

    @Autowired
    public void setQR(QRService qr) {
        this.qr = qr;
    }

    @Autowired
    public void setEm(EntityManager em) {
        this.em = em;
    }


    @Override
    public String redirectUrl(String url) {

        try (Session session = em.unwrap(Session.class)) {


            String actualUrl = jedis.get(url);
            System.out.println(actualUrl);
            if (actualUrl != null) {
                return actualUrl;
            }

            Base64.Decoder decoder = Base64.getDecoder();

            byte[] decoded = decoder.decode(url);
            Long decodedId = Long.parseLong(new String(decoded, StandardCharsets.UTF_8));

            Urls dbUrl = session.createQuery("from Urls where id=:id and expiryDate > current date and isExpired = false", Urls.class)
                    .setParameter("id", decodedId)
                    .uniqueResult();

            if (dbUrl == null) throw new CustomException("Shortened Url Does Not Exist", HttpStatus.NOT_FOUND);
            jedis.set(url, dbUrl.getUrl());
            return dbUrl.getUrl();

        }
        catch (NumberFormatException e){
            throw new CustomException("Invalid URL", HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    @Transactional
    public ShortenUrlResponse getShortenedUrl(String url) {


        try (Session session = em.unwrap(Session.class)) {
            if(url == null) throw new CustomException("Url cannot be empty", HttpStatus.BAD_REQUEST);

            //check if url is valid
            if(!checkIfExists(url)) throw new CustomException("Not a Valid URL", HttpStatus.NOT_FOUND);

            Base64.Encoder encoder = Base64.getEncoder();
            //check if url already exists
            Urls existing = checkIfExistsInDB(url, session);

            String image = null;
            //generate qr code if possible
            try {
                image = generateQRCode(url);
            }
            catch (WriterException | IOException e) {
                System.out.println(e.getMessage());
            }

            //if url already exists, return existing code
            if (existing != null) {
                byte[] encodeText = String.valueOf(existing.getId()).getBytes();
                String encodedText = encoder.encodeToString(encodeText);
                return new ShortenUrlResponse(image, encodedText);
            }

            //else add url to db
            Urls newUrl = addNewUrl(url, session);
            byte[] encodeText = String.valueOf(newUrl.getId()).getBytes();
            String encodedText = encoder.encodeToString(encodeText);

            return new ShortenUrlResponse(image, encodedText);

        }
        catch (CustomException e){
            throw e;
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private Urls checkIfExistsInDB(String url, Session session) {
        try  {
            return session.createQuery("from Urls where url=:url and expiryDate > current date", Urls.class)
                    .setParameter("url", url)
                    .uniqueResult();
        }
        catch (Exception e){
            return null;
        }
    };


    private Urls addNewUrl(String url, Session session) {

        Urls newUrl = new Urls();
        newUrl.setUrl(url);
        newUrl.setCreatedDate(new Date(System.currentTimeMillis()));
        newUrl.setExpiryDate(new Date(System.currentTimeMillis() + 604800000));
        newUrl.setExpired(false);
        session.persist(newUrl);
        session.flush();

        return newUrl;
    };




    private String generateQRCode(String url) throws WriterException, IOException {
        return qr.generateQRCode(url);
    };


    private boolean checkIfExists(String url) throws Exception {
        URL url1 = new URL(url);
        HttpURLConnection connection = (HttpURLConnection) url1.openConnection();
        connection.setRequestProperty("User-Agent",
                "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 "
                        + "(KHTML, like Gecko) Chrome/120.0 Safari/537.36");


        connection.setInstanceFollowRedirects(true);
        connection.setConnectTimeout(5000);
        connection.setReadTimeout(5000);


        connection.setRequestMethod("GET");
        connection.setRequestProperty("Accept-Encoding", "identity");
        connection.setRequestProperty("Range", "bytes=0-0");

        int responseCode = connection.getResponseCode();
        return responseCode >= 200 && responseCode < 400; // Accept redirects + OK responses

    }

}
