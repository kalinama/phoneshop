package com.es.phoneshop.web.validator;


import com.es.core.phone.entity.Phone;
import com.es.core.phone.service.PhoneService;
import com.es.phoneshop.web.entity.QuickOrderEntriesForm;
import com.es.phoneshop.web.entity.QuickOrderEntry;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import javax.annotation.Resource;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;
import java.util.Optional;

@Component
public class QuickOrderEntriesFormValidator implements Validator {

    @Resource(name = "defaultPhoneService")
    private PhoneService phoneService;

    public static final String FRACTIONAL_NUMBER = "quantity.fractional";
    public static final String NOT_NUMBER = "quantity.not.number";
    public static final String NOT_POSITIVE_NUMBER = "quantity.not.positive";
    public static final String PHONE_MODEL_NOT_FOUND = "model.not.found";


    @Override
    public boolean supports(Class<?> aClass) {
        return QuickOrderEntriesForm.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        QuickOrderEntriesForm quickOrderEntriesForm = (QuickOrderEntriesForm) o;

        for (int entryIndex = 0; entryIndex < quickOrderEntriesForm.getEntries().size(); entryIndex++) {
            QuickOrderEntry entry = quickOrderEntriesForm.getEntries().get(entryIndex);

            if (entry.getModel().isEmpty() && entry.getQuantity().isEmpty()) {
                continue;
            }

            validateQuantity(entry.getQuantity(), entryIndex, errors);
            validateModel(entry.getModel(), entryIndex, errors);
        }
    }

    private void validateQuantity(String quantityInput, int entryIndex, Errors errors) {
        int quantity;
        double quantityFractional;
        try {
            Locale locale = LocaleContextHolder.getLocale();
            NumberFormat numberFormat = NumberFormat.getInstance(locale);
            quantityFractional = numberFormat.parse(quantityInput).doubleValue();
            quantity = (int) quantityFractional;
        } catch (ParseException e) {
            errors.rejectValue("entries[" + entryIndex + "].quantity", NOT_NUMBER);
            return;
        }

        if (quantityFractional != quantity) {
            errors.rejectValue("entries[" + entryIndex + "].quantity", FRACTIONAL_NUMBER);
        }

        if (quantity <= 0) {
            errors.rejectValue("entries[" + entryIndex + "].quantity", NOT_POSITIVE_NUMBER);
        }
    }

    private void validateModel(String modelInput, int entryIndex, Errors errors) {
        Optional<Phone> phone = phoneService.getByModel(modelInput);
        if (!phone.isPresent())
            errors.rejectValue("entries[" + entryIndex + "].model", PHONE_MODEL_NOT_FOUND);
    }
}
