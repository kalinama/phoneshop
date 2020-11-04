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
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;
import java.util.stream.Collectors;

@Controller
@RequestMapping(value = "/ajaxCart")
public class AjaxCartController {
    @Resource
    private CartService httpCartService;

    @Resource(name = "inputForAddToCartValidator")
    private Validator quantityValidator;

    private final static String MESSAGE_SUCCESS = "Successful added to cart";

    @GetMapping
    public @ResponseBody MiniCart getCart(HttpSession httpSession) {
        Cart cart = httpCartService.getCart(httpSession);
        return new MiniCart(cart.getTotalQuantity(), cart.getTotalCost());
    }

    @PostMapping
    public @ResponseBody AddToCartResponse addPhone(@RequestBody InputForAddToCart inputForAddToCart,
                               BindingResult bindingResult, Locale locale, HttpSession httpSession) throws ParseException {
        inputForAddToCart.setLocale(locale);
        quantityValidator.validate(inputForAddToCart, bindingResult);

        if (bindingResult.hasErrors()) {
            String message = bindingResult.getAllErrors().stream()
                   .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .collect(Collectors.joining(" && "));
            return new AddToCartResponse(message, false);
        }

        Cart cart = httpCartService.getCart(httpSession);
        httpCartService.addPhone(cart, inputForAddToCart.getPhoneId(), parseQuantity(inputForAddToCart, locale));
        MiniCart miniCart = new MiniCart(cart.getTotalQuantity(), cart.getTotalCost());
        return new AddToCartResponse(miniCart, MESSAGE_SUCCESS, true);
    }

    private Long parseQuantity(InputForAddToCart inputForAddToCart, Locale locale) throws ParseException {
        return NumberFormat.getInstance(locale)
                .parse(inputForAddToCart.getQuantity())
                .longValue();
    }

}
