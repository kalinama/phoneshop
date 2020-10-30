package com.es.core.order.service.exception;

public class OutOfStockException extends Exception {
    private long availableStock;

    public OutOfStockException(long availableStock) {
        this.availableStock = availableStock;
    }

    public long getAvailableStock() {
        return availableStock;
    }
}
