package com.es.core.cart.service.exception;

public class PhoneNotFoundException extends RuntimeException{
    private long id;

    public PhoneNotFoundException(long id) { this.id = id; }

    public long getId() {
        return id;
    }
}
