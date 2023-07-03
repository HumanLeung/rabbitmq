package com.example.springrabbitmqtutorial.springdemo.service;

import cn.hutool.core.date.DateUtil;
import cn.hutool.json.JSONUtil;
import com.example.springrabbitmqtutorial.springdemo.callback.OrderCallback;
import com.example.springrabbitmqtutorial.springdemo.config.RabbitMQConfig;
import com.example.springrabbitmqtutorial.springdemo.entity.OrderEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.UUID;

@Service
@Slf4j
public class OrderService {
    private final RabbitTemplate rabbitTemplate;
    private final OrderCallback orderCallback;

    @PostConstruct
    @Scope("prototype")
    public void init(){
        rabbitTemplate.setConfirmCallback(orderCallback);
        rabbitTemplate.setMandatory(true);
    }
    @Autowired
    public OrderService( RabbitTemplate rabbitTemplate,OrderCallback orderCallback){
        this.rabbitTemplate = rabbitTemplate;
        this.orderCallback = orderCallback;
    }
    public void createOrder(){
        OrderEntity order = new OrderEntity();
        order.setId(UUID.randomUUID().toString());
        order.setOrderCode("SOD123125235434");
        order.setReceiver("John Wick");
        String body = JSONUtil.toJsonStr(order);
        String exchangeName = "fanout-order-exchange";
        String routeKey = "";
        rabbitTemplate.convertAndSend(exchangeName,routeKey,body);
    }
    public void createTopicOrder(){
        String body = exchangeInfoFill();
        String exchangeName = "topic-order-exchange";
        String routeKey = "com.sms.email.message";
        rabbitTemplate.convertAndSend(exchangeName,routeKey,body);
    }
    public String exchangeInfoFill(){
        OrderEntity order = new OrderEntity();
        order.setId(UUID.randomUUID().toString());
        order.setOrderCode("SOD123125235434");
        order.setReceiver("John Wick");
        return JSONUtil.toJsonStr(order);
    }
    public void createOrderIn10() throws InterruptedException {

        rabbitTemplate.convertAndSend(RabbitMQConfig.ORDER_EXCHANGE, "order", "测试订单延迟");
        System.out.println("发送消息：" + DateUtil.now());
        Thread.sleep(10000);
    }
}
