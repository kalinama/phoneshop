package com.es.core.phone.entity;

public class Stock {
    private Phone phone;
    private Long stock;
    private Long reserved;

    public Stock(Phone phone, Long stock, Long reserved) {
        this.phone = phone;
        this.stock = stock;
        this.reserved = reserved;
    }

    public Stock() {
    }

    public Phone getPhone() {
        return phone;
    }

    public void setPhone(Phone phone) {
        this.phone = phone;
    }

    public Long getStock() {
        return stock;
    }

    public void setStock(Long stock) {
        this.stock = stock;
    }

    public Long getReserved() {
        return reserved;
    }

    public void setReserved(Long reserved) {
        this.reserved = reserved;
    }

}
