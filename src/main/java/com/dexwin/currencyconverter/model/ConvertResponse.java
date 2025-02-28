package com.dexwin.currencyconverter.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class ConvertResponse {
    private String sourceCurrency;
    private String targetCurrency;
    private Double amount;
    private Double exchangeRate;
    private Double convertedAmount;
}
