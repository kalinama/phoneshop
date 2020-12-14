package com.es.phoneshop.web.entity;

public class ErrorMessageForQuickOrder {
    private String message;
    private TypeOfErrorMessageForQuickOrder type;

    public ErrorMessageForQuickOrder(String message, TypeOfErrorMessageForQuickOrder type) {
        this.message = message;
        this.type = type;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public TypeOfErrorMessageForQuickOrder getType() {
        return type;
    }

    public void setType(TypeOfErrorMessageForQuickOrder type) {
        this.type = type;
    }
}
