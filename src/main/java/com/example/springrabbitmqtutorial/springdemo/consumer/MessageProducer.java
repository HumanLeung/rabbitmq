package com.example.springrabbitmqtutorial.springdemo.consumer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.UUID;

@Component
@Scope("prototype")
@Slf4j
public class MessageProducer implements RabbitTemplate.ConfirmCallback,RabbitTemplate.ReturnCallback {

    private final RabbitTemplate rabbitTemplate;

    @Autowired
    public MessageProducer(RabbitTemplate rabbitTemplate){
        this.rabbitTemplate = rabbitTemplate;
    }

    @PostConstruct
    @Scope("prototype")
    private void init(){
        rabbitTemplate.setConfirmCallback(this);
        rabbitTemplate.setMandatory(true);
        rabbitTemplate.setReturnCallback(this);
    }


    public void sendMessage(String message){
        CorrelationData correlationData = new CorrelationData(UUID.randomUUID().toString());
        rabbitTemplate.convertAndSend("confirm.exchange","key1",message + "key1",correlationData);
        log.info("发送消息 id为:{} 内容为{}",correlationData.getId(),message + "ke1");
        CorrelationData correlationData2 = new CorrelationData(UUID.randomUUID().toString());
        rabbitTemplate.convertAndSend("confirm.exchange","key2",message+"key2",correlationData2);
        log.info("发送消息 id 为:{}内容为{}",correlationData2.getId(),message+"key2");
    }

    @Override
    public void confirm(CorrelationData correlationData, boolean ack, String cause) {
    String id = correlationData != null ? correlationData.getId() : "";
    if (ack) {
        log.info("交换机收到消息确认成功， id: {}",id);
    } else {
        log.error("消息 id: {} 未成功投递到交换机，原因是：{}", id,cause);
    }
    }

    @Override
    public void returnedMessage(Message message, int replyCode, String replyText, String exchange, String routingKey) {
     log.info("消息:{}被服务器退回，退回原因：{}，交换机是：{}，路由 key:{}",
             new String(message.getBody()),replyText,replyCode,routingKey);
    }
}
