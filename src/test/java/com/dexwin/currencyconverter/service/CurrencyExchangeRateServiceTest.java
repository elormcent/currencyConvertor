package com.dexwin.currencyconverter.service;

import com.dexwin.currencyconverter.exception.CurrencyConvertorException;
import com.dexwin.currencyconverter.exception.ExchangeRateExtractionException;
import com.dexwin.currencyconverter.model.ConvertResponse;
import com.dexwin.currencyconverter.model.Error;
import com.dexwin.currencyconverter.model.ExchangeRateResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Value;

import java.io.IOException;
import java.lang.reflect.Field;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CurrencyExchangeRateServiceTest {
//Attempting 100% coverage

    @InjectMocks
    private CurrencyExchangeRateService currencyExchangeRateService;

    @Mock
    private HttpClient httpClient;

    @Mock
    HttpResponse<String> httpResponse;

    @Test
    void convert() throws IOException, InterruptedException, NoSuchFieldException, IllegalAccessException {
        String source = "USD";
        String target = "EUR";
        double amount = 100;
        injectBaseUrl();
        injectEndpoint();
        injectKey();

        String jsonResponse = "{\"success\":true,\"quotes\":{\"USDEUR\":0.85}}";

        when(httpClient.send(any(HttpRequest.class), eq(HttpResponse.BodyHandlers.ofString())))
                .thenReturn(httpResponse);
        when(httpResponse.body()).thenReturn(jsonResponse);

        ConvertResponse convertResponse = currencyExchangeRateService.convert(source, target, amount);

        assertNotNull(convertResponse);
        assertEquals(source, convertResponse.getSourceCurrency());
        assertEquals(target, convertResponse.getTargetCurrency());
        assertEquals(amount, convertResponse.getAmount());
        System.out.println(convertResponse.getExchangeRate());
        assertEquals(0.85, convertResponse.getExchangeRate());
        assertEquals(85, convertResponse.getConvertedAmount());
    }

    @Test
    void convertApiResponseThrowsException() throws IOException, InterruptedException, NoSuchFieldException, IllegalAccessException {
        String source = "USD";
        String target = "EUR";
        double amount = 100;

        injectBaseUrl();
        injectEndpoint();
        injectKey();

        String jsonResponse = "{\"success\":false,\"error\":{\"code\":101,\"type\":\"invalid_access_key\", \"info\":\"you need key\"}}";
        when(httpResponse.body()).thenReturn(jsonResponse);

        when(httpClient.send(any(HttpRequest.class), eq(HttpResponse.BodyHandlers.ofString())))
                .thenReturn(httpResponse);

        CurrencyConvertorException exception = assertThrows(CurrencyConvertorException.class, () ->
                currencyExchangeRateService.convert(source, target, amount)
        );
        Error error = new Error(101, "invalid_access_key", "you need key");

        assertEquals("Failed to fetch exchange rate. Error Info : \n %s".formatted(error), exception.getMessage());
    }

    @Test
    void testExchangeRateExtractionThrowsException() throws IOException, InterruptedException, NoSuchFieldException, IllegalAccessException {
        String source = "USD";
        String target = "EUR";
        double amount = 100;

        injectBaseUrl();
        injectEndpoint();
        injectKey();

        String jsonResponse = "{\"success\":true}";

        when(httpClient.send(any(HttpRequest.class), eq(HttpResponse.BodyHandlers.ofString())))
                .thenReturn(httpResponse);
        when(httpResponse.body()).thenReturn(jsonResponse);

        ExchangeRateExtractionException exception = assertThrows(ExchangeRateExtractionException.class, () ->
                currencyExchangeRateService.convert(source, target, amount)
        );

        assertEquals("An error occurred while extracting exchange rate information. No quotes found for exchange rate key USDEUR" , exception.getMessage());
    }

    private void injectBaseUrl() throws IllegalAccessException, NoSuchFieldException {
        Field field = CurrencyExchangeRateService.class.getDeclaredField("baseUrl");
        field.setAccessible(true);
        field.set(currencyExchangeRateService, "https://test");
    }

    private void injectEndpoint() throws IllegalAccessException, NoSuchFieldException {
        Field field = CurrencyExchangeRateService.class.getDeclaredField("endpoint");
        field.setAccessible(true);
        field.set(currencyExchangeRateService, "live");
    }

    private void injectKey() throws IllegalAccessException, NoSuchFieldException {
        Field field = CurrencyExchangeRateService.class.getDeclaredField("accessKey");
        field.setAccessible(true);
        field.set(currencyExchangeRateService, "f9999ea05f70fe30dd3f0725970211b7");
    }

}