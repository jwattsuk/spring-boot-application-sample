package com.jwattsuk.sample.exception;

import java.io.Serial;

public class ServiceInitialiseException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = -1888691212851356729L;


    public ServiceInitialiseException(final Throwable cause) {
        super(cause);
    }

    public ServiceInitialiseException(String message, Throwable cause) {
        super(message, cause);
    }
}
