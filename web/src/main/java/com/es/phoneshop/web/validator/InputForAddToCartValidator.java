package com.es.phoneshop.web.validator;

import com.es.phoneshop.web.entity.InputForAddToCart;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.text.NumberFormat;
import java.text.ParseException;

@Component
public class InputForAddToCartValidator implements Validator {

    @Override
    public boolean supports(Class<?> aClass) {
        return InputForAddToCart.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
      InputForAddToCart inputForAddToCart = (InputForAddToCart) o;
        int quantity;
        double quantityFractional;
        try {
            NumberFormat numberFormat = NumberFormat.getInstance(inputForAddToCart.getLocale());
            quantityFractional = numberFormat.parse(inputForAddToCart.getQuantity()).doubleValue();
            quantity = (int) quantityFractional;
        } catch (ParseException e) {
            errors.reject("quantity","Not a number");
            return;
        }

        if (quantityFractional != quantity){
            errors.reject("quantity","Can't enter fractional number");
        }

        if (quantity <= 0) {
            errors.reject("quantity","Can't add 0 or negative number of items");
        }
    }
}
