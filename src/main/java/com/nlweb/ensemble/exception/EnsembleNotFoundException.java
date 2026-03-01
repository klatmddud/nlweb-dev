package com.nlweb.ensemble.exception;

import com.nlweb.common.exception.custom.BaseException;

public class EnsembleNotFoundException extends BaseException {

    public EnsembleNotFoundException(String message) {
        super("ENSEMBLE_NOT_FOUND", message);
    }

}
