package com.dexwin.currencyconverter.service;

import com.dexwin.currencyconverter.exception.CurrencyConvertorException;
import com.dexwin.currencyconverter.exception.ExchangeRateExtractionException;
import com.dexwin.currencyconverter.model.ConvertResponse;
import com.dexwin.currencyconverter.model.ExchangeRateResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

/**
 * TODO: Implementation of this class has to be backed by https://api.exchangerate.host/latest?base=EUR&symbols=AUD,CAD,CHF,CNY,GBP,JPY,USD
 */
@Service
@Slf4j
public class CurrencyExchangeRateService implements CurrencyService {
    @Value("${app.api.baseurl}")
    private String baseUrl;

    @Value("${app.api.endpoint}")
    private String endpoint;

    @Value("${app.api.access.key}")
    private String accessKey;

    @Override
    public ConvertResponse convert(String source, String target, double amount) throws IOException, InterruptedException {
        String requestUrl = generateRequestUrl(source, target, amount);
        HttpRequest request = buildHttpRequest(requestUrl);

        HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
        ExchangeRateResponse exchangeRateResponse = parseExchangeRateResponse(response);

        validateExchangeRateResponse(exchangeRateResponse);

        String exchangeRateKey = generateExchangeRateKey(source, target);
        double exchangeRateValue = extractExchangeRateValue(exchangeRateResponse, exchangeRateKey);
        double convertedAmount = calculateConvertedAmount(amount, exchangeRateValue);

        return buildConvertResponse(source, target, amount, exchangeRateValue, convertedAmount);
    }

    private String generateRequestUrl(String source, String target, double amount) {
        return String.format(
                "%s%s?access_key=%s&source=%s&currencies=%s",
                baseUrl,
                endpoint,
                accessKey,
                source,
                target
        );
    }

    private static HttpRequest buildHttpRequest(String requestUrl) {
        return HttpRequest.newBuilder()
                .uri(URI.create(requestUrl))
                .GET()
                .build();
    }

    private static ExchangeRateResponse parseExchangeRateResponse(HttpResponse<String> response) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(response.body(), ExchangeRateResponse.class);
    }

    private static void validateExchangeRateResponse(ExchangeRateResponse exchangeRateResponse) {
        if (!exchangeRateResponse.isSuccess()) {
            throw new CurrencyConvertorException(exchangeRateResponse.getError());
        }
    }

    private static String generateExchangeRateKey(String source, String target) {
        return String.format("%s%s", source, target);
    }

    private static double extractExchangeRateValue(ExchangeRateResponse exchangeRateResponse, String exchangeRateKey) {
        if (exchangeRateResponse.getQuotes() == null || exchangeRateResponse.getQuotes().isEmpty()) {
            throw new ExchangeRateExtractionException("No quotes found for exchange rate key " + exchangeRateKey);
        }
        return exchangeRateResponse.getQuotes().getOrDefault(exchangeRateKey, 0.0);
    }

    private static double calculateConvertedAmount(double amount, double exchangeRateValue) {
        return amount * exchangeRateValue;
    }

    private static ConvertResponse buildConvertResponse(String source, String target, double amount, double exchangeRateValue, double convertedAmount) {
        return ConvertResponse.builder()
                .sourceCurrency(source)
                .targetCurrency(target)
                .amount(amount)
                .exchangeRate(exchangeRateValue)
                .convertedAmount(convertedAmount)
                .build();
    }
}
