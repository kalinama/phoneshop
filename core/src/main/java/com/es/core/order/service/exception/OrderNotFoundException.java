package com.es.core.order.service.exception;

public class OrderNotFoundException extends RuntimeException{
    private final String secureId;

    public OrderNotFoundException(String secureId) { this.secureId = secureId; }

    public String getId() {
        return secureId;
    }
}
