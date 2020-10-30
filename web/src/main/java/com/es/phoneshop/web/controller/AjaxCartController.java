package com.es.phoneshop.web.controller;

import com.es.core.cart.entity.Cart;
import com.es.core.cart.service.CartService;
import com.es.phoneshop.web.entity.AddToCartResponse;
import com.es.phoneshop.web.entity.InputForAddToCart;
import com.es.phoneshop.web.entity.MiniCart;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;
import java.util.stream.Collectors;

@Controller
@RequestMapping(value = "/ajaxCart")
public class AjaxCartController {
    @Resource
    private CartService httpCartService;

    @Resource
    private Validator quantityValidator;

    @InitBinder("inputForAddToCart")
    protected void initBinder(WebDataBinder binder) {
        binder.setValidator(quantityValidator);
    }


    @GetMapping
    public @ResponseBody MiniCart getCart() {
        Cart cart = httpCartService.getCart();
        return new MiniCart(cart.getTotalQuantity(), cart.getTotalCost());
    }

    @PostMapping
    public @ResponseBody AddToCartResponse addPhone(@RequestBody InputForAddToCart inputForAddToCart,
                               BindingResult bindingResult, Locale locale) throws ParseException {
        Cart cart = httpCartService.getCart();
        inputForAddToCart.setLocale(locale);
        quantityValidator.validate(inputForAddToCart, bindingResult);

        String message;
        if (bindingResult.hasErrors()) {
           message = bindingResult.getAllErrors().stream()
                   .map(DefaultMessageSourceResolvable::getDefaultMessage).collect(Collectors.joining("&&"));
            return new AddToCartResponse(message, false);
        }
        message = "Successful added to cart";
        httpCartService.addPhone(inputForAddToCart.getPhoneId(),
                NumberFormat.getInstance(locale).parse(inputForAddToCart.getQuantity()).longValue());
        MiniCart miniCart = new MiniCart(cart.getTotalQuantity(), cart.getTotalCost());
        return new AddToCartResponse(miniCart, message, true);
    }



}
