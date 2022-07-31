/**
 * Command Line Current Converter
 *
 * Author: David Salter 2022
 */
package com.davidsalter.currency;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@Service
public class CurrencyService {

    @Value("${currency.currency-details.file.key:src/main/resources/currency.csv}")
    private String currencyCsvFileName;
    public static final int COUNTRY_POSITION = 0;
    public static final int CURRENCY_NAME_POSITION = 1;
    public static final int COUNTRY_CODE_POSITION = 2;
    public static final int COUNTRY_RATE_POSITION = 3;
    public static final String CURRENCY_CONVERSION_FORMAT = "%.2f %s converts to %.2f %s %s";
    private final List<Currency> currencyList = new ArrayList<>();

    @PostConstruct
    /*
     * Loads the currency details from file.  Currency details are stored in a CSV file with the following format:
     *  CountryName, Currency Name, Currency Code, Exchange Rate
     *
     * An example CSV file would have rows in it such as:
     *  United Arab Emirates,Dirhams, AED, 7.2104
     */
    private void loadCurrencies() {
        try (Stream<String> stream = Files.lines(Paths.get(currencyCsvFileName))) {

            stream.forEach(c -> {
                String[] values = c.split(",");
                Currency currency = new Currency(values[COUNTRY_POSITION],
                        values[CURRENCY_NAME_POSITION],
                        values[COUNTRY_CODE_POSITION],
                        Float.parseFloat(values[COUNTRY_RATE_POSITION]));
                currencyList.add(currency);
            });

        } catch (IOException e) {
            throw new RuntimeException("Error loading the currency conversion file.");
        }
    }

    /**
     * Validates that the source currency code and target currency code exist within the details that have been loaded.
     * @see #loadCurrencies()
     * @param sourceCurrencyCode The source currency code in ISO4217 format, e.g. "AED"
     * @param targetCurrencyCode The target currency code in ISO4217 format, e.g. "AED"
     * @throws IllegalArgumentException Thrown if either the source or target currency codes are not recognised.
     */
    public void validateRequest(String sourceCurrencyCode, String targetCurrencyCode) throws IllegalArgumentException {
        Optional<Currency> sourceCurrency = getCurrencyByCode(sourceCurrencyCode);
        if (sourceCurrency.isEmpty()) {
            throw new IllegalArgumentException(String.format("Source currency code (%s) is invalid.", sourceCurrencyCode));
        }

        Optional<Currency> targetCurrency = getCurrencyByCode(targetCurrencyCode);
        if (targetCurrency.isEmpty()) {
            throw new IllegalArgumentException(String.format("Target currency code (%s) is invalid.", targetCurrencyCode));
        }
    }

    /**
     * Retrieves a Currency object from the currencyList based upon a specific currency code.
     * @see #loadCurrencies()
     * @see #currencyList
     * @param code The currency code to find in ISO4217 format.
     * @return A Currency object for the specified currency code.
     */
    private Optional<Currency> getCurrencyByCode(String code) {
        return currencyList.stream()
                .filter(c-> code.equals(c.getCurrencyCode()))
                .findAny();
    }

    /**
     * Calculated a String message returning details of converting an amount of money in one currency to another currency.
     * The resulting currency conversion is rounded to 2 decimal places in the results.
     * @param sourceCurrency The amount of money to convert.
     * @param sourceCurrencyCode The source currency in ISO4217 format.
     * @param targetCurrencyCode The target currency in ISO4217 format.
     * @return Formatted string in the format CURRENCY_CONVERSION_FORMAT
     */
    public String calculateCurrencyExchange(float sourceCurrency, String sourceCurrencyCode, String targetCurrencyCode) {
        Optional<Currency> sourceConversion = getCurrencyByCode(sourceCurrencyCode);
        Optional<Currency> targetConversion = getCurrencyByCode(targetCurrencyCode);

        if (sourceConversion.isEmpty() || targetConversion.isEmpty()) {
            return "Unable to convert currencies as source/target conversion details not found.";
        }

        // Calculate the value in GBP.
        float gbp = sourceCurrency / sourceConversion.get().getConversionRate();

        // Convert GBP to the target currency.
        float targetAmount = gbp * targetConversion.get().getConversionRate();

        return String.format(CURRENCY_CONVERSION_FORMAT,
                sourceCurrency,
                sourceCurrencyCode,
                targetAmount,
                targetConversion.get().getCountry(),
                targetConversion.get().getCurrencyName());
    }
}
