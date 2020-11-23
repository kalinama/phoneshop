package com.es.phoneshop.web.controller.pages;

import com.es.core.cart.service.CartService;
import com.es.core.cart.service.exception.PhoneNotFoundException;
import com.es.core.order.entity.Order;
import com.es.core.order.service.OrderService;
import com.es.core.order.service.exception.EmptyCartException;
import com.es.core.order.service.exception.OutOfStockException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import javax.swing.border.EmptyBorder;
import javax.validation.Valid;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
@RequestMapping(value = "/order")
public class OrderPageController {

    @Resource(name = "defaultOrderService")
    private OrderService orderService;

    @Resource(name = "httpSessionCartService")
    private CartService cartService;

    @GetMapping
    public String getOrder(HttpSession httpSession, Model model) {
        Order order = orderService.createOrder(cartService.getCart(httpSession));
        Map<Long,Long> availableStockForOutOfStockPhones = orderService.getAvailableStocksForOutOfStockPhones(order);

        if (!availableStockForOutOfStockPhones.isEmpty()) {
            model.addAttribute("availableStockForOutOfStockPhones", availableStockForOutOfStockPhones);
        }

        model.addAttribute("order", order);
        return "order";
    }

    @DeleteMapping("{id}")
    public String deleteOrderItem(@PathVariable Long id, HttpSession httpSession) {
        cartService.remove(cartService.getCart(httpSession), id);
        return "redirect:/order";
    }

    @PutMapping
    public String updateOrder(@RequestParam(value = "phoneId") List<Long> phoneIds,
                                 @RequestParam(value = "quantity") List<Long> quantities,
                                 HttpSession httpSession) {
        Map<Long,Long> phonesToUpdate =  IntStream.range(0, phoneIds.size()).boxed()
                .collect(Collectors.toMap(phoneIds::get, quantities::get));
        cartService.update(cartService.getCart(httpSession), phonesToUpdate);
        return "redirect:/order";
    }

    @PostMapping
    public String placeOrder(@Valid @ModelAttribute("order") Order order, BindingResult bindingResult,
                             HttpSession httpSession, Model model) {
        orderService.completeOrder(order, cartService.getCart(httpSession));
        model.addAttribute("order", order);

        if (bindingResult.hasErrors()) {
            return "order";
        }

        try {
            orderService.placeOrder(order);
        } catch (OutOfStockException e) {
            return "redirect:/order";
        }

        cartService.clearCart(httpSession);
        return "redirect:/orderOverview/" + order.getSecureId();
    }

    @ExceptionHandler(EmptyCartException.class)
    public String handleCustomException(EmptyCartException ex, Model model) {
        return "/errors/emptyCartException";
    }
}
