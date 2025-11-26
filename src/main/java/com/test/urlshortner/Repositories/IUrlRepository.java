package com.test.urlshortner.Repositories;

import com.test.urlshortner.Models.ShortenUrlResponse;

public interface IUrlRepository {

    ShortenUrlResponse getShortenedUrl(String url);
    String redirectUrl(String url);
}
