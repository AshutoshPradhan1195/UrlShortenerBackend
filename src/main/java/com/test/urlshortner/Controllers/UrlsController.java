package com.test.urlshortner.Controllers;

import com.test.urlshortner.Models.*;
import com.test.urlshortner.Models.ResponseBody;
import com.test.urlshortner.Repositories.IUrlRepository;
import com.test.urlshortner.Repositories.UrlsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
public class UrlsController {

    private IUrlRepository urlsRepository;

    @Autowired
    public UrlsController(IUrlRepository urlsRepository) {
        this.urlsRepository = urlsRepository;
    }

    @PostMapping()
    public Response<ResponseBody<ShortenUrlResponse>> addNewUrl(@RequestBody Urls url){
        try{
            ShortenUrlResponse shortened = urlsRepository.getShortenedUrl(url.getUrl());
            ResponseBody<ShortenUrlResponse> body = new ResponseBody<>(shortened, "");
            return new Response<>(body,  HttpStatus.OK);
        }
        catch (CustomException e){
            ResponseBody<ShortenUrlResponse> body = new ResponseBody<>(null, e.getMessage());
            return new Response<>(body, e.getStatus());
        }
        catch(Exception e){
            ResponseBody<ShortenUrlResponse> body = new ResponseBody<>(null, e.getMessage());
            return new Response<>(body,  HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("{url}")
    public Response<ResponseBody<String>> redirect(@PathVariable String url){
        try{
            String redirectUrl = urlsRepository.redirectUrl(url);
            ResponseBody<String> body = new ResponseBody<>(redirectUrl, "");
            HttpHeaders headers = new HttpHeaders();
            headers.setLocation(new URI(redirectUrl));
            return new Response<>(body, headers, HttpStatus.OK);
        }
        catch (CustomException e){
            ResponseBody<String> body = new ResponseBody<>(null, e.getMessage());
            return new Response<>(body, e.getStatus());
        }
        catch(Exception e){
            ResponseBody<String> body = new ResponseBody<>(null, e.getMessage());
            return new Response<>(body,  HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
