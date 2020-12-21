package com.npokrista.moneytransfer.service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class IncorrectValueException extends IllegalArgumentException {
    public IncorrectValueException(String msg){
        super(msg);
    }
}
