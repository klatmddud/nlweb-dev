package com.nlweb.timeslot.exception;

import com.nlweb.common.exception.custom.BaseException;

public class TimeslotNotFoundException extends BaseException {

    public TimeslotNotFoundException(String message) {
        super("TIMESLOT_NOT_FOUND", message);
    }

}
