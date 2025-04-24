package org.tasks.myshop.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.tasks.myshop.dto.InnerOrder;
import org.tasks.myshop.dto.OrderDto;
import org.tasks.myshop.service.OrderService;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping
    public String getOrders(Model model) {
        Map<Long, InnerOrder> orders = orderService.findAll();
        model.addAttribute("orders", orders);

        return "orders";
    }

    @GetMapping("/{id}")
    public String getOrder(Model model, @PathVariable("id") Long orderId) {
        model = orderService.getModelOrdersById(model, orderId);

        return "order";
    }

}
