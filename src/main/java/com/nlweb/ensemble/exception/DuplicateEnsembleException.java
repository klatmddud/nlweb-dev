package com.nlweb.ensemble.exception;

import com.nlweb.common.exception.custom.BaseException;

public class DuplicateEnsembleException  extends BaseException{

    public DuplicateEnsembleException(String message) {
        super("DUPLICATE_ENSEMBLE", message);
    }

}
