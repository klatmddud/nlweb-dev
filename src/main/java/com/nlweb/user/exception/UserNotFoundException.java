package com.nlweb.user.exception;

import com.nlweb.common.exception.custom.BaseException;

public class UserNotFoundException extends BaseException {

    public UserNotFoundException(String message) {
        super("USER_NOT_FOUND", message);
    }

    public UserNotFoundException(String message, Throwable cause) {
        super("USER_NOT_FOUND", message, cause);
    }

}
