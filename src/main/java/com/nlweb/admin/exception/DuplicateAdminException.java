package com.nlweb.admin.exception;

import com.nlweb.common.exception.custom.BaseException;

public class DuplicateAdminException extends BaseException {

    public DuplicateAdminException(String message) {
        super("ADMIN_DUPLICATE", message);
    }

}
