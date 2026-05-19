package com.svault.vault.exceptions;

public class TooManyAttemptsException extends RuntimeException {

    public TooManyAttemptsException() {
        super("Too many attempts. Try again later.");
    }

    public TooManyAttemptsException(String message) {
        super(message);
    }
}