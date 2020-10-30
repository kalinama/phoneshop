package com.es.core.cart.service;

import com.es.core.cart.entity.Cart;
import com.es.core.cart.entity.CartItem;
import com.es.core.cart.service.exception.PhoneNotFoundException;
import com.es.core.order.service.exception.OutOfStockException;
import com.es.core.phone.dao.PhoneDao;
import com.es.core.phone.dao.StockDao;
import com.es.core.phone.entity.Phone;
import com.es.core.phone.entity.Stock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Map;
import java.util.Optional;

@Service
public class HttpSessionCartService implements CartService {
    @Resource
    private PhoneDao phoneDao;

    @Resource
    private Cart cart;

    @Override
    public Cart getCart() {
        return cart;
    }

    @Override
    public void addPhone(Long phoneId, Long quantity) {
        synchronized (cart) {
            if (quantity <= 0) {
                throw new IllegalArgumentException();
            }

            Optional<Phone> phone = phoneDao.get(phoneId);

            if (!phone.isPresent()) {
                throw new PhoneNotFoundException(phoneId);
            }

            Optional<CartItem> existedCartItem = cart.getCartItems().stream()
                    .filter(cartItem -> cartItem.getPhone().equals(phone.get()))
                    .findAny();

            if (!existedCartItem.isPresent()) {
                cart.getCartItems().add(new CartItem(phone.get(), quantity));
            } else {
                existedCartItem.get().setQuantity(existedCartItem.get().getQuantity() + quantity);
            }
            recalculateCart(cart);
        }

    }

    private void recalculateCart(Cart cart) {
        Long totalQuantity = cart.getCartItems().stream()
                .mapToLong(CartItem::getQuantity)
                .sum();
        BigDecimal totalCost = cart.getCartItems().stream()
                .map(item -> item.getPhone().getPrice().multiply(new BigDecimal(item.getQuantity())))
                .reduce(BigDecimal::add)
                .orElse(new BigDecimal(0));

        cart.setTotalQuantity(totalQuantity);
        cart.setTotalCost(totalCost);
    }

    @Override
    public void update(Map<Long, Long> items) {
        throw new UnsupportedOperationException("TODO");
    }

    @Override
    public void remove(Long phoneId) {
        throw new UnsupportedOperationException("TODO");
    }
}
