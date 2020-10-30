package com.es.phoneshop.web.entity;

public class AddToCartResponse {

    private MiniCart miniCart;
    private String message;
    private boolean isAdded;

    public AddToCartResponse(MiniCart miniCart, String message, boolean isAdded) {
        this.message = message;
        this.isAdded = isAdded;
        this.miniCart = miniCart;
    }

    public AddToCartResponse(String message, boolean isAdded) {
        this.message = message;
        this.isAdded = isAdded;
    }

    public MiniCart getMiniCart() {
        return miniCart;
    }

    public void setMiniCart(MiniCart miniCart) {
        this.miniCart = miniCart;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isAdded() {
        return isAdded;
    }

    public void setAdded(boolean isSuccess) {
        isSuccess = isSuccess;
    }
}
