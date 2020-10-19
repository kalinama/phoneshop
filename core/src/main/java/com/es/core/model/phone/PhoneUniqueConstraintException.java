package com.es.core.model.phone;

public class PhoneUniqueConstraintException extends UniqueConstraintException {
    private final static String MESSAGE = "Combination of model and brand values should be unique";
    PhoneUniqueConstraintException(){
        super(MESSAGE);
    }
}
