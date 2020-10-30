package com.es.phoneshop.web.controller.pages;

import javax.annotation.Resource;

import com.es.core.phone.enums.SortOrder;
import com.es.core.phone.enums.SortParameter;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.es.core.phone.dao.PhoneDao;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping(value = "/productList")
public class ProductListPageController {
    @Resource
    private PhoneDao jdbcPhoneDao;

    private static final int PHONES_QUANTITY_PER_PAGE = 10;

    @GetMapping
    public String showProductList(@RequestParam(value = "page", defaultValue = "1") String page,
                                  @RequestParam(value = "sort", defaultValue = "brand") String sortParameter,
                                  @RequestParam(value = "order", defaultValue = "asc") String sortOrder,
                                  @RequestParam(value = "query", required = false) String query, Model model) {

        int offset = (Integer.parseInt(page) - 1) * PHONES_QUANTITY_PER_PAGE;

        model.addAttribute("maxPageNumber", getMaxPageQuantity(jdbcPhoneDao.getQuantity(query)));
        model.addAttribute("phones", jdbcPhoneDao.findAll(offset, PHONES_QUANTITY_PER_PAGE,
                query, SortParameter.valueOf(sortParameter), SortOrder.valueOf(sortOrder)));
        return "productList";
    }

    private int getMaxPageQuantity(int phonesQuantity) {
        if (phonesQuantity % PHONES_QUANTITY_PER_PAGE == 0) {
            return phonesQuantity / PHONES_QUANTITY_PER_PAGE;
        } else {
            return phonesQuantity / PHONES_QUANTITY_PER_PAGE + 1;
        }
    }
}
