package com.es.phoneshop.web.validator;

import com.es.phoneshop.web.entity.InputQuantityUnit;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.text.NumberFormat;
import java.text.ParseException;

@Component
public class InputQuantityValidator implements Validator {

    public static final String FRACTIONAL_NUMBER = "Can't enter fractional number";
    public static final String NOT_NUMBER = "Not a number";
    public static final String NOT_POSITIVE_NUMBER = "Can't add 0 or negative number of items";

    @Override
    public boolean supports(Class<?> aClass) {
        return InputQuantityUnit.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
      InputQuantityUnit inputQuantityUnit = (InputQuantityUnit) o;
        int quantity;
        double quantityFractional;
        try {
            NumberFormat numberFormat = NumberFormat.getInstance(inputQuantityUnit.getLocale());
            quantityFractional = numberFormat.parse(inputQuantityUnit.getQuantity()).doubleValue();
            quantity = (int) quantityFractional;
        } catch (ParseException e) {
            errors.reject("quantity", NOT_NUMBER);
            return;
        }

        if (quantityFractional != quantity) {
            errors.reject("quantity", FRACTIONAL_NUMBER);
        }

        if (quantity <= 0) {
            errors.reject("quantity", NOT_POSITIVE_NUMBER);
        }
    }
}
