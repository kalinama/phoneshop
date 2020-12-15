package com.es.phoneshop.web.entity;

public class QuickOrderEntry {
    private String model;
    private Long quantity;

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public Long getQuantity() {
        return quantity;
    }

    public void setQuantity(Long quantity) {
        this.quantity = quantity;
    }
}
