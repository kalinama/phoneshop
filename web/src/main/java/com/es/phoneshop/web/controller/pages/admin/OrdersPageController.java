package com.es.phoneshop.web.controller.pages.admin;

import com.es.core.order.dao.OrderDao;
import com.es.core.order.entity.Order;
import com.es.core.order.entity.OrderStatus;
import com.es.core.order.service.OrderService;
import com.es.core.order.service.exception.OrderNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Map;

@Controller
@RequestMapping(value = "/admin/orders")
public class OrdersPageController {

    @Resource(name = "jdbcOrderDao")
    private OrderDao orderDao;

    @Resource(name = "defaultOrderService")
    private OrderService orderService;

    @GetMapping
    public String showOrders(Model model) {
        model.addAttribute("orders", orderDao.findAll());
        return "admin/orderList";
    }

    @GetMapping("{id}")
    public String showOrder(@PathVariable Long id, Model model) {
        model.addAttribute("order", orderDao.getById(id)
                .orElseThrow(()-> new OrderNotFoundException(id.toString())));
        return "admin/orderDetails";
    }

    @PostMapping(path = "{id}")
    @ResponseStatus(HttpStatus.OK)
    public void changeOrderStatus(@PathVariable Long id,
                                  @RequestBody Map<String,String> body) {
        Order order = orderDao.getById(id)
                .orElseThrow(()-> new OrderNotFoundException(id.toString()));
        orderService.changeOrderStatus(order, OrderStatus.valueOf(body.get("orderStatus").toUpperCase()));
    }

    @ExceptionHandler(OrderNotFoundException.class)
    public String handleCustomException(OrderNotFoundException ex, Model model) {
        model.addAttribute("id", ex.getId());
        return "/errors/orderNotFoundException";
    }
}
