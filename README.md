# Java Currency Converter

David Salter.
July 2022

## Introduction
This project is a simple Java Currency Converter that can convert from one currency to another.

Currencies that can be converted are defined within the `resources/currency.csv` file.

When the application is launched, the `CurrencyService` Spring Bean is instantiated and the `loadCurrencies` method
is invoked via the `@PostConstruct` mechanism. This method parses the `currency.csv` file storing it within a list of
`Currency` objects.

## Technologies

* Java 17
* Spring Boot 2.7.2
* Maven

## Running the application

The application can be executed via Maven with the following command:

```
mvn spring-boot:run
```

This command will load the currenct conversion properties from the in build `resources/currency.csv` file. 
To load the currency details from an external file, specify the `currency.currency-details.file` property at runtime.

From Maven, execute:

```
mvn spring-boot:run  \
-Dspring-boot.run.arguments="--currency.currency-details.file.key=<path_to_new_file>"
```

## Logging

A simple Aspect has been added in the `LoggingAspect` class to use SimpleLog4J logging to log entry into all the methods within any `@Service` implementations.  This could be extended if required to log more information, such as:

* Exceptions being thrown
* Input parameters
* Return values

This is left as is to aid development, but is easily extended if required.

All logging for the application is output to the file `spring.log` which is defined by the application property `logging.file.path`

## Example run of the application

```
$ mvn spring-boot:run

Please enter a source amount:
10.77
Please enter a source currency code:
AED
Please enter a target currency code:
BGN
10.77 AED converts to 3.90 Bulgaria Leva

```