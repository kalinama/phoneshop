package com.es.phoneshop.web.controller;

import com.es.core.cart.entity.Cart;
import com.es.core.cart.service.CartService;
import com.es.phoneshop.web.controller.helper.BindingResultHelper;
import com.es.phoneshop.web.entity.AddToCartResponse;
import com.es.phoneshop.web.entity.InputQuantityUnit;
import com.es.phoneshop.web.entity.MiniCart;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;

@Controller
@RequestMapping(value = "/ajaxCart")
public class AjaxCartController {
    @Resource(name = "httpSessionCartService")
    private CartService cartService;

    @Resource(name = "inputQuantityValidator")
    private Validator quantityValidator;

    @Resource(name = "defaultBindingResultHelper")
    private BindingResultHelper bindingResultHelper;

    private final static String MESSAGE_SUCCESS = "Successful added to cart";

    @GetMapping
    public @ResponseBody MiniCart getCart(HttpSession httpSession) {
        Cart cart = cartService.getCart(httpSession);
        return new MiniCart(cart.getTotalQuantity(), cart.getTotalCost());
    }

    @PostMapping
    public @ResponseBody AddToCartResponse addPhone(@RequestParam(name = "phoneId") Long phoneId,
                                                    @RequestParam(name = "quantity") String quantity,
                                                    Locale locale, HttpSession httpSession) throws ParseException {
       InputQuantityUnit input = new InputQuantityUnit(quantity, locale);
       BindingResult bindingResult = bindingResultHelper.getBindingResult(input, quantityValidator);

       if (bindingResult.hasErrors()) {
            String message = bindingResultHelper.getErrorMessage(bindingResult);
            return new AddToCartResponse(message, false);
        }

        Cart cart = cartService.getCart(httpSession);
        cartService.addPhone(cart, phoneId, parseQuantity(input));
        MiniCart miniCart = new MiniCart(cart.getTotalQuantity(), cart.getTotalCost());
        return new AddToCartResponse(miniCart, MESSAGE_SUCCESS, true);
    }

    private Long parseQuantity(InputQuantityUnit quantityUnit) throws ParseException {
        return NumberFormat.getInstance(quantityUnit.getLocale())
                .parse(quantityUnit.getQuantity())
                .longValue();
    }

}
