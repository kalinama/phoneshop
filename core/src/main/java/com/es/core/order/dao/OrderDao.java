package com.es.core.order.dao;

import com.es.core.order.entity.Order;

import java.util.Optional;

public interface OrderDao {
    Optional<Order> get(String secureId);
    void save(Order order);
}
