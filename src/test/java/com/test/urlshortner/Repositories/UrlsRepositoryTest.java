package com.test.urlshortner.Repositories;

import com.test.urlshortner.Models.Response;
import com.test.urlshortner.Models.ShortenUrlResponse;
import com.test.urlshortner.Models.Urls;
import jakarta.persistence.EntityManager;
import org.hibernate.Session;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;

import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UrlsRepositoryTest {

    private IUrlRepository urlsRepository;
    private EntityManager em;
    private  String[] urls = new String[]
            {
                    "https://www.google.com/search?q=java+test+junit&oq=java+test+j&gs_lcrp=EgZjaHJvbWUqCAgCEAAYFhgeMgYIABBFGDkyBwgBEAAYgAQyCAgCEAAYFhgeMggIAxAAGBYYHjIICAQQABgWGB4yCAgFEAAYFhgeMggIBhAAGBYYHjIICAcQABgWGB4yCAgIEAAYFhgeMggICRAAGBYYHtIBCDYwNTJqMGo3qAIAsAIA&sourceid=chrome&ie=UTF-8",
                    "https://www.projectpro.io/article/artificial-intelligence-project-ideas/461",
                    "https://www.google.com/search?q=hrllo&oq=hrllo&gs_lcrp=EgZjaHJvbWUyBggAEEUYOdIBCDM3NDhqMGo5qAIGsAIB8QUDkpeDq4SEmg&sourceid=chrome&ie=UTF-8",
                    "https://colab.google/",
                    "https://chatgpt.com/",
                    "https://mangafire.to/home",
                    "https://myflixerz.to/",
                    "https://fitgirl-repacks.site/"
            };

    @Autowired
    public UrlsRepositoryTest( IUrlRepository urlsRepository,  EntityManager em) {
        this.urlsRepository = urlsRepository;
        this.em = em;
    }

    @ParameterizedTest()
    @ValueSource(strings = {
            "https://junit.org/",
            "https://www.projectpro.io/article/artificial-intelligence-project-ideas/461",
            "https://www.google.com/search?q=hrllo&oq=hrllo",
            "https://colab.google/",
            "https://chatgpt.com/",
            "https://mangafire.to/home",
            "https://myflixerz.to/",
            "https://fitgirl-repacks.site/",
            "https://www.instagram.com/stories/pramuditaaudas/"
    })
    void getShortenedUrl(String url) {
        System.out.println(url);
        try {
            ShortenUrlResponse shortenedUrl = urlsRepository.getShortenedUrl(url);
            assertNotNull(shortenedUrl);
        }
        catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    void redirectUrl() {
        try (Session session = em.unwrap(Session.class)) {
            session.createQuery("from Urls where expiryDate < current_date", Urls.class)
                    .list()
                    .forEach(url -> {
                        try {
                            URL urlGetter = new  URL(url.getUrl());
                            HttpURLConnection conection = (HttpURLConnection) urlGetter.openConnection();
                            conection.setRequestMethod("HEAD");
                            int responseCode = conection.getResponseCode();
                            assertEquals(HttpURLConnection.HTTP_OK, responseCode);
                            System.out.println(url.getUrl());
                        }
                        catch (Exception e) {
                            fail(e.getMessage());
                        }
                    });
        }
        catch (Exception e) {
            fail(e.getMessage());
        }
    }
}