package com.mercedesbenz.exchangerate.model;

public class ExchangeRateException extends RuntimeException {
    public ExchangeRateException(String symbol, String message) {
        super(String.format("%s : %s",message, symbol));
    }
    public ExchangeRateException(String message) {
        super(message);
    }
}
