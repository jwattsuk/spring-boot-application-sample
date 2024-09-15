package com.jwattsuk.sample.exception;

public class TradeNotFoundException extends RuntimeException {

    public TradeNotFoundException(String id) {
        super("Could not find trade " + id);
    }
}
