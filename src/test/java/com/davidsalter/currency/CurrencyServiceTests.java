/**
 * Command Line Current Converter
 *
 * Author: David Salter 2022
 */
package com.davidsalter.currency;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.springframework.test.util.AssertionErrors.assertEquals;

/*
 * Unit tests for the CurrencyService.  These tests check the validation of parameters passed into the service methods,
 * and also that the service can generate a correct currency conversion output.
 */
@SpringBootTest(classes = {CurrencyService.class})
public class CurrencyServiceTests {

    @Autowired
    CurrencyService currencyService;

    @Test
    public void shouldFailIfSourceCurrencyIsNotRecognised() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> currencyService.validateRequest("INVALID", "AED"));
    }

    @Test
    public void shouldFailIfTargetCurrencyIsNotRecognised() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> currencyService.validateRequest("AED", "INVALID"));
    }

    @Test
    public void shouldSucceedIfSourceAndTargetCurrenciesAreValid() {
        currencyService.validateRequest("BGN", "AED");
    }

    @Test
    public void shouldConvertFromSourceCurrencyToTargetCurrency() {
        String results = currencyService.calculateCurrencyExchange(10.77f, "AED", "BGN");
        String expected = "10.77 AED converts to 3.90 Bulgaria Leva";
        assertEquals("Invalid currency conversion", expected, results);

    }
}
