package com.nlweb.program.exception;

import com.nlweb.common.exception.custom.BaseException;

public class ProgramApplyNotAvailableException extends BaseException {

    public ProgramApplyNotAvailableException(String message) {
        super("PROGRAM_APPLY_NOT_AVAILABLE", message);
    }

}
