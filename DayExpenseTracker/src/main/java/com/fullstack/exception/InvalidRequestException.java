package com.fullstack.exception;

public class InvalidRequestException extends RuntimeException{

    public InvalidRequestException(String msg){
        super(msg);
    }
}
