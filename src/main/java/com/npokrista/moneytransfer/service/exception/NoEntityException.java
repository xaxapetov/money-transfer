package com.npokrista.moneytransfer.service.exception;

public class NoEntityException extends NullPointerException {
    public NoEntityException(String msg) {
        super(msg);
    }
}
