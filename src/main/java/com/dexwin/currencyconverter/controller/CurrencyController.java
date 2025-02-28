package com.dexwin.currencyconverter.controller;

import com.dexwin.currencyconverter.model.ConvertResponse;
import com.dexwin.currencyconverter.service.CurrencyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;


@RestController
public class CurrencyController {

    private final CurrencyService currencyService;

    @Autowired
    public CurrencyController(final CurrencyService currencyService) {
        this.currencyService = currencyService;
    }

    @GetMapping(value = "currencies/convert", produces = MediaType.APPLICATION_JSON_VALUE)
    public ConvertResponse convert(
            @RequestParam("source") String source,
            @RequestParam("target") String target,
            @RequestParam("amount") double amount) throws IOException, InterruptedException {

        return currencyService.convert(source, target, amount);
    }

}
