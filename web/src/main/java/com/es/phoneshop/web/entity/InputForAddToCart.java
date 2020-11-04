package com.es.phoneshop.web.entity;

import java.util.Locale;

public class InputForAddToCart {

    private Long phoneId;
    private String quantity;
    private Locale locale;

    public InputForAddToCart(Long phoneId, String quantity, Locale locale) {
        this.phoneId = phoneId;
        this.quantity = quantity;
        this.locale = locale;
    }

    public InputForAddToCart() {
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public Locale getLocale() {
        return locale;
    }

    public void setLocale(Locale locale) {
        this.locale = locale;
    }

    public Long getPhoneId() {
        return phoneId;
    }

    public void setPhoneId(Long phoneId) {
        this.phoneId = phoneId;
    }
}
