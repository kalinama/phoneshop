package com.es.core.order.service;

import com.es.core.cart.entity.Cart;
import com.es.core.order.entity.Order;
import com.es.core.order.service.exception.OutOfStockException;

public interface OrderService {
    Order createOrder(Cart cart);
    void placeOrder(Order order) throws OutOfStockException;
}
