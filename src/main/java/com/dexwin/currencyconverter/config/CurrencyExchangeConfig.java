package com.dexwin.currencyconverter.config;

import com.dexwin.currencyconverter.service.CurrencyExchangeRateService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.http.HttpClient;

@Configuration
public class CurrencyExchangeConfig {
    @Bean
    CurrencyExchangeRateService currencyExchangeRateService(@Value("${app.api.baseurl}") String baseUrl,
                                                            @Value("${app.api.endpoint}") String endpoint,
                                                            @Value("${app.api.access.key}") String accessKey,
                                                            HttpClient httpClient) {
        return new CurrencyExchangeRateService(baseUrl, endpoint, accessKey, httpClient);
    }
}
