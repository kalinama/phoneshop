package com.es.phoneshop.web.controller.pages;

import com.es.core.cart.entity.Cart;
import com.es.core.cart.service.CartService;
import com.es.phoneshop.web.controller.helper.BindingResultHelper;
import com.es.phoneshop.web.entity.InputQuantityUnit;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.DataBinder;
import org.springframework.validation.Validator;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping(value = "/cart")
public class CartPageController {
    @Resource
    private CartService httpCartService;

    @Resource(name = "inputQuantityValidator")
    private Validator quantityValidator;

    @Resource(name = "defaultBindingResultHelper")
    private BindingResultHelper bindingResultHelper;

    @GetMapping
    public String getCart(HttpSession httpSession, Model model) {
        model.addAttribute("cart",httpCartService.getCart(httpSession));
        return "cart";
    }

    @DeleteMapping("/{id}")
    public String deleteCartItem(@PathVariable Long id, HttpSession httpSession) {
        Cart cart = httpCartService.getCart(httpSession);
        httpCartService.remove(cart, id);
        return "redirect:/cart";
    }

    @PutMapping
    public String updateCart(@RequestParam(value = "phoneId") List<Long> phoneIds,
                           @RequestParam(value = "quantity") List<String> quantities,
                           Locale locale, HttpSession httpSession, Model model) throws ParseException {
        Map<Long, String> errors = new HashMap<>();
        Map<Long, Long> rightInput = new HashMap<>();
        Cart cart = httpCartService.getCart(httpSession);

        for (int i = 0; i < phoneIds.size(); i++) {
            InputQuantityUnit input = new InputQuantityUnit(quantities.get(i), locale);
            BindingResult bindingResult = bindingResultHelper.getBindingResult(input, quantityValidator);
            if (bindingResult.hasErrors()) {
                errors.put(phoneIds.get(i), bindingResultHelper.getErrorMessage(bindingResult));
            } else {
                rightInput.put(phoneIds.get(i), parseQuantity(input));
            }
        }

        if (errors.isEmpty()) {
            httpCartService.update(cart, rightInput);
        } else {
            model.addAttribute("errors", errors);
        }
        model.addAttribute("cart", cart);
        return "cart";
    }

    private Long parseQuantity(InputQuantityUnit quantityUnit) throws ParseException {
        return NumberFormat.getInstance(quantityUnit.getLocale())
                .parse(quantityUnit.getQuantity())
                .longValue();
    }

}
