package com.es.phoneshop.web.entity;

import java.math.BigDecimal;

public class MiniCart {
    private Long totalQuantity;
    private BigDecimal totalCost;

    public MiniCart(Long totalQuantity, BigDecimal totalCost) {
        this.totalQuantity = totalQuantity;
        this.totalCost = totalCost;
    }

    public MiniCart() {
    }

    public Long getTotalQuantity() {
        return totalQuantity;
    }

    public void setTotalQuantity(Long totalQuantity) {
        this.totalQuantity = totalQuantity;
    }

    public BigDecimal getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(BigDecimal totalCost) {
        this.totalCost = totalCost;
    }
}
