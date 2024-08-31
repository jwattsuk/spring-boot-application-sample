package com.jwattsuk.sample.exception;

public class TradeNotFoundException extends RuntimeException {

    public TradeNotFoundException(Long id) {
        super("Could not find trade " + id);
    }
}
