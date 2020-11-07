package com.es.phoneshop.web.controller.helper;

import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

public interface BindingResultHelper {
    BindingResult getBindingResult(Object target, Validator validator);
    String getErrorMessage(BindingResult bindingResult);
}
