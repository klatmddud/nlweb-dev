package com.nlweb.auth.exception;

import com.nlweb.common.exception.custom.BaseException;

public class InvalidCredentialsException extends BaseException {

    public InvalidCredentialsException(String message) {
        super("INVALID_CREDENTIALS", message);
    }

}
