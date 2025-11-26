package com.test.urlshortner;

import com.test.urlshortner.Filter.LogginFilter;
import com.test.urlshortner.Repositories.UrlsRepository;
import jakarta.servlet.Filter;
import org.apache.catalina.security.SecurityConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.UnifiedJedis;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Base64;

@SpringBootApplication
public class UrlShortnerApplication {


    //add api key for adding urls
    //create qr generation
    public static void main(String[] args) {
       ApplicationContext context =  SpringApplication.run(UrlShortnerApplication.class, args);
    }

    @Bean
    UnifiedJedis jedis() {
        return new UnifiedJedis("redis://redis:6379");
//        return new UnifiedJedis("http://localhost:6379/");


    }

    @Bean
    public FilterRegistrationBean<Filter> filterRegistrationBean() {
        FilterRegistrationBean<Filter> filterRegistrationBean = new FilterRegistrationBean<>();
        filterRegistrationBean.setFilter(new LogginFilter());
        filterRegistrationBean.addUrlPatterns("/*");
        filterRegistrationBean.addInitParameter("apiKey", "apiKey");
        filterRegistrationBean.setOrder(1);
        return filterRegistrationBean;
    }

}
