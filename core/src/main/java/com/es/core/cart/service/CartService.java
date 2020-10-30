package com.es.core.cart.service;

import com.es.core.cart.entity.Cart;
import com.es.core.order.service.exception.OutOfStockException;
import com.es.core.phone.entity.Phone;

import java.util.Map;

public interface CartService {

    Cart getCart();

    void addPhone(Long phoneId, Long quantity);

    /**
     * @param items
     * key: {@link Phone#id}
     * value: quantity
     */
    void update(Map<Long, Long> items);

    void remove(Long phoneId);
}