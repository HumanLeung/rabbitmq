package com.example.springrabbitmqtutorial.springdemo.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * @author Administrator
 */
@Component
@Slf4j
@Scope("prototype")
public class MyCallBack implements RabbitTemplate.ConfirmCallback,RabbitTemplate.ReturnCallback {
    @Override
    public void confirm(CorrelationData correlationData, boolean ack, String cause) {
        String id = correlationData != null ? correlationData.getId() : "";
        if (ack){
            log.info("交换机已经收到 id 为：{}的消息",id);
        }else{
            log.info("交换机还未收到 id 为：{}消息，由于原因：{}",id,cause);
        }
    }

    public void returnedMessage(Message message, int replyCode, String replyText, String exchange, String routingKey) {
        log.error(" 消息 {}, 被交换机{} 退回 ， 退回原因:{}, 路由key:{}",new
                String(message.getBody()),exchange,replyText,routingKey);
    }
}
