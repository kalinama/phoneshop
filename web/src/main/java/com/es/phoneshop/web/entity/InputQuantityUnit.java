package com.es.phoneshop.web.entity;

import java.util.Locale;

public class InputQuantityUnit {

    private String quantity;
    private Locale locale;

    public InputQuantityUnit(String quantity, Locale locale) {
        this.quantity = quantity;
        this.locale = locale;
    }

    public InputQuantityUnit() {
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

}
