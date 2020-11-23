package com.es.core.order.dao;

import com.es.core.order.entity.Order;

import java.util.List;
import java.util.Optional;

public interface OrderDao {
    Optional<Order> getBySecureId(String secureId);
    Optional<Order> getById(Long id);
    void save(Order order);
    List<Order> findAll();

}
