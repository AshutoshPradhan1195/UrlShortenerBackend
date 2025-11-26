package com.test.urlshortner.Models;

import java.awt.image.BufferedImage;

public class ShortenUrlResponse {
    String image;
    String url;

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public String getImage() {
        return image;
    }
    public void setImage(String image) {
        this.image = image;
    }

    public ShortenUrlResponse(String image, String url) {
        this.image = image;
        this.url = url;
    }
}
