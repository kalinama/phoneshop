package com.es.phoneshop.web.validator;

import com.es.phoneshop.web.entity.InputQuantityUnit;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.validation.Errors;

import java.util.Locale;

import static org.junit.Assert.assertFalse;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class InputQuantityUnitValidatorTest {
    @Mock
    private Errors errors;

    private final InputQuantityValidator validator = new InputQuantityValidator();
    private final Locale locale = new Locale("en");

    public static final String FRACTIONAL_NUMBER = "Can't enter fractional number";
    public static final String NOT_NUMBER = "Not a number";
    public static final String NOT_POSITIVE_NUMBER = "Can't add 0 or negative number of items";


    @Test
    public void getNumberFromQuantityParamTestSuccess() {
        InputQuantityUnit input = new InputQuantityUnit("2", locale);

        validator.validate(input, errors);
        assertFalse(errors.hasErrors());
    }

    @Test
    public void getNumberFromQuantityParamNotNumberError() {
        InputQuantityUnit input = new InputQuantityUnit("hh", locale);

        validator.validate(input, errors);
        verify(errors).reject("quantity", NOT_NUMBER);
    }

    @Test
    public void getNumberFromQuantityParamFractionalNumberError() {
        InputQuantityUnit input = new InputQuantityUnit("1.9", locale);

        validator.validate(input, errors);
        verify(errors).reject("quantity", FRACTIONAL_NUMBER);
    }

    @Test
    public void getNumberFromQuantityParamNegativeNumberError() {
        InputQuantityUnit input = new InputQuantityUnit("-1", locale);

        validator.validate(input, errors);
        verify(errors).reject("quantity", NOT_POSITIVE_NUMBER);
    }

}
