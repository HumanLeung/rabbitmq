package com.example.springrabbitmqtutorial.rawdemo.basic.exchange.direct;

import com.example.springrabbitmqtutorial.rawdemo.basic.common.RabbitmqUtils;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

/**
 * @author Administrator
 */
public class ReceiveLogsDirect01 {
    public static final String EXCHANGE_NAME = "direct_logs";
    public static final String CONSOLE = "console";

    public static void main(String[] args) throws IOException, TimeoutException {
        Channel channel = RabbitmqUtils.getChannel();
        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.DIRECT);
        channel.queueDeclare(CONSOLE,false,false,false,null);
        channel.queueBind(CONSOLE,EXCHANGE_NAME,"info");
        channel.queueBind(CONSOLE,EXCHANGE_NAME,"warning");


        //接收消息
        DeliverCallback deliverCallback = (consumerTag, message) -> {
            System.out.println("ReceiveLogsDirect01----控制台打印接收到的消息："+new String(message.getBody(), StandardCharsets.UTF_8));
        };

        channel.basicConsume(CONSOLE,true,deliverCallback,consumerTag -> {});
    }
}
