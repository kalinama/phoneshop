package com.es.core.model.phone;

public class UniqueConstraintException extends RuntimeException{
    UniqueConstraintException(String message){
        super(message);
    }
}
