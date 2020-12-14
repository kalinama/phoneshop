package com.es.phoneshop.web.controller.pages;

import javax.annotation.Resource;

import com.es.core.phone.enums.SortOrder;
import com.es.core.phone.enums.SortParameter;
import com.es.core.phone.service.PhoneService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping(value = "/productList")
public class ProductListPageController {
    @Resource(name = "defaultPhoneService")
    private PhoneService phoneService;

    private static final int PHONES_QUANTITY_PER_PAGE = 10;

    @GetMapping
    public String showProductList(@RequestParam(value = "page", defaultValue = "1") String page,
                                  @RequestParam(value = "sort", defaultValue = "brand") String sortParameter,
                                  @RequestParam(value = "order", defaultValue = "asc") String sortOrder,
                                  @RequestParam(value = "query", required = false) String query, Model model) {

        int offset = (Integer.parseInt(page) - 1) * PHONES_QUANTITY_PER_PAGE;
        int phonesQuantity = phoneService.getQuantity(query);

        model.addAttribute("maxPageNumber", getMaxPageQuantity(phonesQuantity));
        model.addAttribute("allPhonesQuantity", phonesQuantity);
        model.addAttribute("phones", phoneService.findAll(offset, PHONES_QUANTITY_PER_PAGE,
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
