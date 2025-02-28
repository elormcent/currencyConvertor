package com.dexwin.currencyconverter.exception;

import com.dexwin.currencyconverter.model.Error;

public class CurrencyConvertorException extends RuntimeException{
    public CurrencyConvertorException(Error message) {
        super("Failed to fetch exchange rate. Error Info : \n %s".formatted(message));
    }
}
