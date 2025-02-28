package com.dexwin.currencyconverter.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Query {
    private String from;
    private String to;
    private double amount;
}
