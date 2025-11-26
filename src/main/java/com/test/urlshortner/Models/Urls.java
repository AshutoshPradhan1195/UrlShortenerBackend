package com.test.urlshortner.Models;

import jakarta.persistence.*;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.sql.Date;


@Component
@Entity
@Scope("prototype")
public class Urls {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(nullable = false, unique = true, length = 5000
    )
    private String url;


    @Column(nullable = false)
    private Date createdDate;

    @Column(nullable = false)
    private Date expiryDate;

    @Column(nullable = false)
    private boolean isExpired;


    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }


    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Date getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(Date expiryDate) {
        this.expiryDate = expiryDate;
    }

    public boolean isExpired() {
        return isExpired;
    }

    public void setExpired(boolean expired) {
        isExpired = expired;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Urls{" +
                "url='" + url + '\'' +
                ", createdDate=" + createdDate +
                ", expiryDate=" + expiryDate +
                ", isExpired=" + isExpired +
                '}';
    }
}
