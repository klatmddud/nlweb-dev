package com.nlweb.amho.exception;

import com.nlweb.common.exception.custom.BaseException;

public class InvalidAmhoException extends BaseException {

    public InvalidAmhoException(String message) {
        super("INVALID_AMHO", message);
    }

    public InvalidAmhoException(String message, Throwable cause) {
        super("INVALID_AMHO", message, cause);
    }

}