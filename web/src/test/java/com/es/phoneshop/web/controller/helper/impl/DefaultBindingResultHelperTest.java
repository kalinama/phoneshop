package com.es.phoneshop.web.controller.helper.impl;

import com.es.phoneshop.web.controller.helper.BindingResultHelper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class DefaultBindingResultHelperTest {

    @Mock
    private BindingResult bindingResult;

    private BindingResultHelper bindingResultHelper = new DefaultBindingResultHelper();

    @Test
    public void testGetMessage(){
        List<ObjectError> errors = new ArrayList<>();
        errors.add(new ObjectError("error1", "message1"));
        errors.add(new ObjectError("error2", "message2"));

        when(bindingResult.getAllErrors()).thenReturn(errors);
        assertEquals("message1 && message2", bindingResultHelper.getErrorMessage(bindingResult));
    }
}