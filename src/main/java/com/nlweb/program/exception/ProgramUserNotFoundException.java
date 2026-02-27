package com.nlweb.program.exception;

import com.nlweb.common.exception.custom.BaseException;

public class ProgramUserNotFoundException extends BaseException {

    public ProgramUserNotFoundException(String message) {
        super("PROGRAM_USER_NOT_FOUND", message);
    }

}
