package com.nlweb.ensemble.exception;

import com.nlweb.common.exception.custom.BaseException;

public class DuplicateSessionException extends BaseException {

    public DuplicateSessionException(String message) {
        super("DUPLICATE_SESSION", message);
    }

}
