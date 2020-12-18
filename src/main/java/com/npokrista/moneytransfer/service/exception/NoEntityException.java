package com.npokrista.moneytransfer.service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class NoEntityException extends IllegalArgumentException {
    public NoEntityException(String msg) {
        super(msg);
    }
}
