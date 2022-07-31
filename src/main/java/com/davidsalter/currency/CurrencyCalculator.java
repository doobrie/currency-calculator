/**
 * Command Line Current Converter
 *
 * Author: David Salter 2022
 */
package com.davidsalter.currency;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

import java.io.Console;

@SpringBootApplication
/*
 * SpringBoot CLI application.
 * The application requests currency conversion details from the user and converts between a
 * source currency and a target currency.
 * The currency details and converted value are printed to standard out.
 * Currency names and exchange rates are stored within a CSV that is loaded at runtime to allow the
 * conversions to take place.
 *
 * The application can be executed via Maven using the following command:
 *  mvn spring-boot:run
 *
 * By default, the application will load currency conversion details from the resources/currency.csv
 * file on the classpath inside the jar. To specify a different file to load details from at runtime,
 * specify the currency.currency-details.file parameter, for example:
 *  mvn spring-boot:run -Dspring-boot.run.arguments="--currency.currency-details.file.key=./new-details.csv"
 */
public class CurrencyCalculator implements CommandLineRunner {

    @Autowired
    CurrencyService currencyService;

    /**
     * Main entry point for the application.
     * @param args Command line arguments.  These are not used by the application.
     */
    public static void main(String[] args) {
        try {
            new SpringApplicationBuilder(CurrencyCalculator.class)
                    .logStartupInfo(false)
                    .run(args);
        } catch (RuntimeException e) {
            System.out.println("Could not load the currency conversion file.  Please ensure this file exists.");
        }
    }

    @Override
    /*
     * CommandLineRunner method that is invoked by Spring once all the Spring Beans and configuration have been
     * loaded.
     * @param args Command line arguments. These are not used by the application.
     */
    public void run(String... args) throws Exception {
        try {
            // Get the currency details from the user
            Console console = System.console();
            System.out.println("Please enter a source amount:");
            float sourceCurrency = Float.parseFloat(console.readLine());
            System.out.println("Please enter a source currency code:");
            String sourceCurrencyCode = console.readLine();
            System.out.println("Please enter a target currency code:");
            String targetCurrencyCode = console.readLine();

            // Validate the currency details
            currencyService.validateRequest(sourceCurrencyCode, targetCurrencyCode);

            // Display the converted currency.
            String output = currencyService.calculateCurrencyExchange(sourceCurrency, sourceCurrencyCode, targetCurrencyCode);
            System.out.println(output);
        } catch (NumberFormatException e) {
            // Thrown if the source amount cannot be converted correctly into floating point.
            System.out.println("The source amount is invalid.  Please enter numbers only.");
        } catch (IllegalArgumentException iae) {
            // Thrown if the source or target currency codes do not exist.
            System.out.println(iae.getMessage());
        }
    }
}
