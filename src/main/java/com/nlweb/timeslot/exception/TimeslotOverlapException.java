package com.nlweb.timeslot.exception;

import com.nlweb.common.exception.custom.BaseException;

public class TimeslotOverlapException extends BaseException {
    public TimeslotOverlapException(String message) {
        super("TIMESLOT_OVERLAP", message);
    }
}
