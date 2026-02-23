package com.nlweb.admin.exception;

import com.nlweb.common.exception.custom.BaseException;

public class AdminNotFoundException extends BaseException {

    public AdminNotFoundException(String message) {
        super("ADMIN_NOT_FOUND", message);
    }

}
