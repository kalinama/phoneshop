package com.es.core.order.service;

import com.es.core.cart.entity.Cart;
import com.es.core.order.entity.Order;
import com.es.core.order.service.exception.OutOfStockException;
import org.springframework.stereotype.Service;

@Service
public class OrderServiceImpl implements OrderService {
    @Override
    public Order createOrder(Cart cart) {
        throw new UnsupportedOperationException("TODO");
    }

    @Override
    public void placeOrder(Order order) throws OutOfStockException {
        throw new UnsupportedOperationException("TODO");
    }
}
