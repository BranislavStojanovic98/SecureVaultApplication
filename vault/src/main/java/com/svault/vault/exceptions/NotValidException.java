package com.svault.vault.exceptions;

import org.springframework.http.HttpStatus;

public class NotValidException extends HttpException {
    public NotValidException() {
        super(HttpStatus.BAD_REQUEST, null);
    }

    public NotValidException(Object data) {
        super(HttpStatus.BAD_REQUEST, data);
    }
}
