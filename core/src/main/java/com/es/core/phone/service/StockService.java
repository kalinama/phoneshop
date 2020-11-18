package com.es.core.phone.service;

import com.es.core.order.service.exception.OutOfStockException;

public interface StockService {
    void reservePhone(Long phoneId, Long quantity) throws OutOfStockException;
    void cancelPhoneReservation(Long phoneId, Long quantity);
    void deliverPhone(Long phoneId, Long quantity);
    long getAvailableStock(Long phoneId);
}
