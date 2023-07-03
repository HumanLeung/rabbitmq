package com.example.springrabbitmqtutorial.springdemo.config;

import cn.hutool.core.date.DateUtil;
import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * @author Administrator
 */
@Component
@RabbitListener(queues = RabbitMQConfig.ORDER_DEAD_LETTER_QUEUE)
public class OrderListener {

    @RabbitHandler
    public void consumer(String body, Message message, Channel channel) throws IOException {
        System.out.println("收到消息："+ DateUtil.now());
        long msgTag = message.getMessageProperties().getDeliveryTag();
        System.out.println("msgTag=" + msgTag);
        System.out.println("message=" + message);
        System.out.println("body=" + body);
        channel.basicAck(msgTag, false);
    }
}
