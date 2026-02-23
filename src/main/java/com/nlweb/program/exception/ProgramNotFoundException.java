package com.nlweb.program.exception;

import com.nlweb.common.exception.custom.BaseException;

public class ProgramNotFoundException extends BaseException {

    public ProgramNotFoundException(String message) {
        super("PROGRAM_NOT_FOUND", message);
    }

}
