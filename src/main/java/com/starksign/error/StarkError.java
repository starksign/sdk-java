package com.starksign.error;

public abstract class StarkError extends RuntimeException {

    public StarkError(String message) {
        super(message);
    }
}
