package com.example.springrabbitmqtutorial.rawdemo.basic.exchange.fanout;

import com.example.springrabbitmqtutorial.rawdemo.basic.common.RabbitmqUtils;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

public class ReceiveLogs01 {

    public static final String EXCHANGE_MAME = "logs";

    public static void main(String[] args) throws IOException, TimeoutException {
        Channel channel = RabbitmqUtils.getChannel();
        String queueName = channel.queueDeclare().getQueue();
        channel.exchangeDeclare(EXCHANGE_MAME,"fanout");

       channel.queueBind(queueName,EXCHANGE_MAME,"");
        System.out.println("等待接收消息，把接收到消息打印在屏幕上..........");

        DeliverCallback deliverCallback = (consumerTag, message) -> {
            System.out.println("ReceiveLogs01----控制台打印接收到的消息："+new String(message.getBody(), StandardCharsets.UTF_8));
        };

        channel.basicConsume(queueName,true,deliverCallback,consumerTag -> {});

    }
}
