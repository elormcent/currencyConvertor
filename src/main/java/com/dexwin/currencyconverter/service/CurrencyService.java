package com.dexwin.currencyconverter.service;

import com.dexwin.currencyconverter.model.ConvertResponse;

import java.io.IOException;

public interface CurrencyService {

    ConvertResponse convert(String source, String target, double amount) throws IOException, InterruptedException;

}
