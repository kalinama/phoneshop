package com.es.phoneshop.web.controller.helper.impl;

import com.es.phoneshop.web.controller.helper.BindingResultHelper;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.validation.DataBinder;
import org.springframework.validation.Validator;

import java.util.stream.Collectors;

@Component
public class DefaultBindingResultHelper implements BindingResultHelper {

    public BindingResult getBindingResult(Object target, Validator validator) {
        DataBinder dataBinder = new DataBinder(target);
        dataBinder.setValidator(validator);
        dataBinder.validate();
        return dataBinder.getBindingResult();
    }

    public String getErrorMessage(BindingResult bindingResult) {
        return bindingResult.getAllErrors().stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.joining(" && "));
    }
}
