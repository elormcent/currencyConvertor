package com.dexwin.currencyconverter.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@AllArgsConstructor
@NoArgsConstructor
@Data
@JsonIgnoreProperties(ignoreUnknown = true) //added this annotation to avoid any unexpected field that might be part of API
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ExchangeRateResponse {
    private boolean success;
    private Long timestamp;
    private String source;
    private Map<String, Double> quotes;
    private Error error;
}
