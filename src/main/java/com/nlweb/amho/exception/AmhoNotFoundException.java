package com.nlweb.amho.exception;

import com.nlweb.common.exception.custom.BaseException;

public class AmhoNotFoundException extends BaseException {

    public AmhoNotFoundException(String message) {
        super("AMHO_NOT_FOUND", message);
    }

}
