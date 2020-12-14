package com.es.phoneshop.web.validator;


import com.es.core.phone.dao.PhoneDao;
import com.es.core.phone.dao.StockDao;
import com.es.core.phone.entity.Phone;
import com.es.core.phone.entity.Stock;
import com.es.core.phone.service.PhoneService;
import com.es.core.phone.service.impl.DefaultPhoneService;
import com.es.phoneshop.web.entity.InputQuantityUnit;
import com.es.phoneshop.web.entity.Model2QuantityInputUnit;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import javax.annotation.Resource;
import java.util.Optional;

@Component
public class Model2QuantityInputValidator implements Validator {

    @Resource(name = "inputQuantityValidator")
    private Validator quantityValidator;

    @Resource(name = "jdbcPhoneDao")
    private PhoneDao phoneDao;

    //@Resource(name = "jdbcStockDao")
    //private StockDao stockDao;

    @Override
    public boolean supports(Class<?> aClass) {
        return Model2QuantityInputUnit.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        Model2QuantityInputUnit model2QuantityInputUnit = (Model2QuantityInputUnit) o;

        quantityValidator.validate(new InputQuantityUnit(model2QuantityInputUnit.getQuantity(),
                model2QuantityInputUnit.getLocale()), errors);

        Optional<Phone> phone = phoneDao.getByModel(model2QuantityInputUnit.getModel());
        if (!phone.isPresent())
            errors.reject("model", "Phone not found");
        /*else {
            Optional<Stock> stock = stockDao.get(phone.get().getId());
            if (stock.get().getStock() < Long.parseLong(model2QuantityInputUnit.getQuantity())) {
                errors.reject("quantity", "Out of stock");
            }
        } */

        }
}
