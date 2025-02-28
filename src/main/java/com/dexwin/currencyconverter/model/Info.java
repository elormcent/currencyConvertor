package com.dexwin.currencyconverter.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Info {
    private long timestamp;
    private double quote;
}
