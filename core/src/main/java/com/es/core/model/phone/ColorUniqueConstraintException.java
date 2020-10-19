package com.es.core.model.phone;

public class ColorUniqueConstraintException extends UniqueConstraintException {
    private final static String MESSAGE = "Code value should be unique";
    ColorUniqueConstraintException(){
        super(MESSAGE);
    }
}
