package com.dexwin.currencyconverter.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Error {
    private Integer code;
    private String type;
    private String info;
}
