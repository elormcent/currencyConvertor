package com.dexwin.currencyconverter.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.http.HttpClient;

@Configuration
public class HttpCall {
    @Bean
    HttpClient httpClient() {
        return HttpClient.newHttpClient();
    }
}
