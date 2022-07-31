/**
 * Command Line Current Converter
 *
 * Author: David Salter 2022
 */
package com.davidsalter.currency;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
/*
 * Simple DTO holding details about country, currency name, currency code and exchange rate.
 * Example values are:
 *  country: United Arab Emirates
 *  currency name: Dirhams
 *  currency code: AED
 *  exchange rate: 7.2104
 */
public class Currency {
    private String country;
    private String currencyName;
    private String currencyCode;
    private float conversionRate;
}
