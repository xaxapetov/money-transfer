package com.npokrista.moneytransfer.service.exception;

public class IncorrectValueException extends NullPointerException{
    public IncorrectValueException(String msg){
        super(msg);
    }
}
