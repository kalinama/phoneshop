package com.es.phoneshop.web.entity;

import java.util.Locale;

public class Model2QuantityInputUnit {
    private String model;
    private String quantity;
    private Locale locale;

    public Model2QuantityInputUnit(String model, String quantity, Locale locale) {
        this.model = model;
        this.quantity = quantity;
        this.locale = locale;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
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
