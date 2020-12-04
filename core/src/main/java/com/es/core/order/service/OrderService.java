package com.es.core.order.service;

import com.es.core.cart.entity.Cart;
import com.es.core.order.entity.Order;
import com.es.core.order.entity.OrderStatus;
import com.es.core.order.service.exception.EmptyCartException;
import com.es.core.order.service.exception.OutOfStockException;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface OrderService {
    Optional<Order> getById(Long id);
    Optional<Order> getBySecureId(String secureId);
    List<Order> findAll();

    Order createOrder(Cart cart) throws EmptyCartException;
    void completeOrder(Order orderWithCustomerData, Cart cart);
    Map<Long,Long> getAvailableStocksForOutOfStockPhones(Order order);
    void placeOrder(Order order) throws OutOfStockException;
    void changeOrderStatus(Order order, OrderStatus orderStatus);
}
