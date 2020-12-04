package com.es.phoneshop.web.controller.pages;

import com.es.core.order.entity.Order;
import com.es.core.order.service.OrderService;
import com.es.core.order.service.exception.OrderNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;

@Controller
@RequestMapping(value = "/orderOverview")
public class OrderOverviewPageController {

    @Resource(name = "defaultOrderService")
    private OrderService orderService;

    @GetMapping("{secureId}")
    public String  get(@PathVariable String secureId, Model model){
        Order order = orderService.getBySecureId(secureId)
                .orElseThrow(()-> new OrderNotFoundException(secureId));
        model.addAttribute("order", order);
        return "orderOverview";
    }

    @ExceptionHandler(OrderNotFoundException.class)
    public String handleCustomException(OrderNotFoundException ex, Model model) {
        model.addAttribute("id", ex.getId());
        return "/errors/orderNotFoundException";
    }
}
