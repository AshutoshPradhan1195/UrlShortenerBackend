package com.test.urlshortner.Models;

public class ResponseBody<T> {
    private T  data;
    private String message;

    public ResponseBody(T data, String message) {
        this.data = data;
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }
    public void setData(T data) {
        this.data = data;
    }
}
