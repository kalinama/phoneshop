package com.es.core.cart.entity;

import com.es.core.phone.entity.Phone;

import java.io.Serializable;

public class CartItem implements Serializable {
    private Phone phone;
    private Long quantity;

    public CartItem(Phone phone, Long quantity) {
        this.phone = phone;
        this.quantity = quantity;
    }

    public Phone getPhone() {
        return phone;
    }

    public void setPhone(Phone phone) {
        this.phone = phone;
    }

    public Long getQuantity() {
        return quantity;
    }

    public void setQuantity(Long quantity) {
        this.quantity = quantity;
    }
}
