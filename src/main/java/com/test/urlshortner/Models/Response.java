package com.test.urlshortner.Models;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;

public class Response<T> extends ResponseEntity<T> {


    public Response(T body, HttpStatusCode status) {
        super(body, status);
    }

    public Response(MultiValueMap<String, String> headers, HttpStatusCode status) {
        super(headers, status);
    }

    public Response(T body, MultiValueMap<String, String> headers, int rawStatus) {
        super(body, headers, rawStatus);
    }

    public Response(T body, MultiValueMap<String, String> headers, HttpStatusCode statusCode) {
        super(body, headers, statusCode);
        System.out.println(headers);

    }
}
