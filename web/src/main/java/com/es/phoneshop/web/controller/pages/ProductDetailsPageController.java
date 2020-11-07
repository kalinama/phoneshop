package com.es.phoneshop.web.controller.pages;

import com.es.core.cart.service.exception.PhoneNotFoundException;
import com.es.core.phone.dao.PhoneDao;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;

@Controller
@RequestMapping(value = "/productDetails")
public class ProductDetailsPageController {

    @Resource
    PhoneDao jdbcPhoneDao;

    @GetMapping("/{id}")
    public String showProductDetails(@PathVariable Long id, Model model) {
        model.addAttribute("phone", jdbcPhoneDao.get(id).orElseThrow(() -> new PhoneNotFoundException(id)));
        return "productDetails";
    }

    @ExceptionHandler(PhoneNotFoundException.class)
    public String handleCustomException(PhoneNotFoundException ex, Model model) {
        model.addAttribute("id", ex.getId());
        return "/errors/productNotFoundException";
    }

}
