package com.example.springrabbitmqtutorial.rawdemo.basic.deadqueue;

import com.example.springrabbitmqtutorial.rawdemo.basic.common.RabbitmqUtils;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeoutException;

/**
 * @author Administrator
 */
public class Consumer01 {
    private static final String NORMAL_EXCHANGE = "normal_exchange";
    private static final String DEAD_EXCHANGE = "dead_exchange";
    public static final String NORMAL_QUEUE = "normal_queue";
    public static final String DEAD_QUEUE = "dead_queue";

    public static void main(String[] args) throws IOException, TimeoutException {
        Channel channel = RabbitmqUtils.getChannel();
        channel.exchangeDeclare(NORMAL_EXCHANGE, BuiltinExchangeType.DIRECT);
        channel.exchangeDeclare(DEAD_EXCHANGE,BuiltinExchangeType.DIRECT);
        channel.queueDeclare(DEAD_QUEUE, false, false, false, null);
        channel.queueBind(DEAD_QUEUE,DEAD_EXCHANGE,"Lee Sing");

        Map<String, Object> params = new HashMap<>();
        params.put("x-message-ttl",10000);
        params.put("x-dead-letter-exchange",DEAD_EXCHANGE);
        params.put("x-dead-letter-routing-key","Lee Sing");
//        arguments.put("x-max-length",6);
        channel.queueDeclare(NORMAL_QUEUE,false,false,false,params);
        channel.queueBind(NORMAL_QUEUE,NORMAL_EXCHANGE,"Ken");


//        DeliverCallback deliverCallback = (consumeTag, message) -> {
//            String msg = new String(message.getBody(), StandardCharsets.UTF_8);
//            if (msg.equals("info5")){
//                System.out.println("Consumer01接收的消息是："+msg+": 此消息被C1拒绝");
//                channel.basicReject(message.getEnvelope().getDeliveryTag(),false);
//            }else{
//                System.out.println("Consumer01接收的消息是："+msg+": 此消息被C1拒绝");
//            }
//        };

        System.out.println("等待接收消息 ........... ");
        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), StandardCharsets.UTF_8);
            System.out.println("Consumer01 接收到消息"+message);
        };

        channel.basicConsume(NORMAL_QUEUE,true,deliverCallback,
                consumerTag -> {});
    }
}
