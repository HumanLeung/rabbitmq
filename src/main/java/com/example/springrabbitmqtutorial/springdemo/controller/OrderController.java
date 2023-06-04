package com.example.springrabbitmqtutorial.springdemo.controller;

import com.example.springrabbitmqtutorial.springdemo.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Administrator
 */
@RestController
@RequestMapping("/order")
public class OrderController {
    private final OrderService orderService;
    @Autowired
    public OrderController(OrderService orderService){
        this.orderService = orderService;
    }
    @GetMapping("/create")
    public void createOrder(){
        orderService.createOrder();
        orderService.createTopicOrder();
    }
}
