package com.nlweb.user.exception;

import com.nlweb.common.exception.custom.BaseException;

public class DuplicateUserException extends BaseException {

    public DuplicateUserException(String message) {
        super("USER_DUPLICATE", message);
    }

    public DuplicateUserException(String message, Throwable cause) {
        super("USER_DUPLICATE", message, cause);
    }

}
