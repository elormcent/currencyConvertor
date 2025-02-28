package com.dexwin.currencyconverter.exception;

public class ExchangeRateExtractionException extends RuntimeException {
    public ExchangeRateExtractionException(String message) {
        super("An error occurred while extracting exchange rate information. %s".formatted(message));
    }
}
