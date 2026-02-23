package com.nlweb.user.exception;

import com.nlweb.common.exception.custom.BaseException;

public class InvalidUserOperationException extends BaseException {

    public InvalidUserOperationException(String message) {
        super("INVALID_USER_OPERATION", message);
    }

}
