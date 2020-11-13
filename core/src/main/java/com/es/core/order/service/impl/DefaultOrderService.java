package com.es.core.order.service.impl;

import com.es.core.cart.entity.Cart;
import com.es.core.order.dao.OrderDao;
import com.es.core.order.entity.Order;
import com.es.core.order.entity.OrderItem;
import com.es.core.order.entity.OrderStatus;
import com.es.core.order.service.OrderService;
import com.es.core.order.service.exception.OutOfStockException;
import com.es.core.phone.service.StockService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class DefaultOrderService implements OrderService {

    @Value("${delivery.price}")
    private BigDecimal deliveryPrice;

    @Resource(name = "defaultStockService")
    private StockService stockService;
    @Resource
    private OrderDao jdbcOrderDao;

    @Override
    public Order createOrder(Cart cart) {
        Order order = new Order();
        List<OrderItem> items = getOrderItemsFromCart(cart, order);

        order.setOrderItems(items);
        order.setSubtotal(cart.getTotalCost());
        order.setDeliveryPrice(deliveryPrice);
        order.setTotalPrice(order.getSubtotal().add(order.getDeliveryPrice()));
        return order;
    }

    @Override
    public void completeOrder(Order orderWithCustomerData, Cart cart) {
        synchronized (orderWithCustomerData) {
            Order order = this.createOrder(cart);
            orderWithCustomerData.setOrderItems(order.getOrderItems());
            orderWithCustomerData.setSubtotal(order.getSubtotal());
            orderWithCustomerData.setDeliveryPrice(order.getDeliveryPrice());
            orderWithCustomerData.setTotalPrice(order.getTotalPrice());

            orderWithCustomerData.setSecureId(UUID.randomUUID().toString());
            orderWithCustomerData.setOrderingDate(LocalDate.now());
            orderWithCustomerData.setStatus(OrderStatus.NEW);
        }
    }

    @Override
    public Map<Long,Long> getAvailableStocksForOutOfStockPhones(Order order) {
        Map<Long, Long> changes = new HashMap<>();
        order.getOrderItems().stream()
                .filter(item -> stockService.getAvailableStock(item.getPhone().getId()) < item.getQuantity())
                .forEach(itemOutOfStock -> changes.put(itemOutOfStock.getPhone().getId(),
                        stockService.getAvailableStock(itemOutOfStock.getPhone().getId())));
        return changes;
    }

    @Override
    @Transactional(rollbackFor = OutOfStockException.class)
    public void placeOrder(Order order) throws OutOfStockException {
        synchronized (order) {
            jdbcOrderDao.save(order);
            for (OrderItem orderItem : order.getOrderItems()) {
                stockService.reservePhone(orderItem.getPhone().getId(), orderItem.getQuantity());
            }
        }
    }

    private List<OrderItem> getOrderItemsFromCart(Cart cart, Order order) {
        return cart.getCartItems().stream()
                .map(cartItem -> {
                    OrderItem orderItem = new OrderItem();
                    orderItem.setPhone(cartItem.getPhone());
                    orderItem.setQuantity(cartItem.getQuantity());
                    orderItem.setOrder(order);
                    return orderItem;
                })
                .collect(Collectors.toList());
    }
}