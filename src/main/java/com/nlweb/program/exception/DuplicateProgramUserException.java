package com.nlweb.program.exception;

import com.nlweb.common.exception.custom.BaseException;

public class DuplicateProgramUserException extends BaseException {

    public DuplicateProgramUserException(String message) {
        super("PROGRAM_USER_DUPLICATE", message);
    }

}
