package com.es.core.cart.service.exception;

import java.util.function.Supplier;

public class PhoneNotFoundException extends RuntimeException{
    private long id;

    public PhoneNotFoundException(long id) { this.id = id; }

    public long getId() {
        return id;
    }
}
