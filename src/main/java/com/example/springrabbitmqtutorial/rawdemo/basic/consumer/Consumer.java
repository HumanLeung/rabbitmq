package com.example.springrabbitmqtutorial.rawdemo.basic.consumer;

import com.example.springrabbitmqtutorial.rawdemo.basic.common.RabbitmqUtils;
import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author Administrator
 */
public class Consumer {

    public static final String QUEUE_NAME = "hello";

    public static void main(String[] args) {
        try(Connection connection = RabbitmqUtils.getConnection();
            Channel channel = connection.createChannel();) {
            DeliverCallback deliverCallback = (consumerTag,message) -> System.out.println(new String(message.getBody()));

            CancelCallback cancelCallback = consumerTag -> System.out.println("message interrupted");

            channel.basicConsume(QUEUE_NAME,true,deliverCallback,cancelCallback);
        } catch (IOException | TimeoutException e) {
            throw new RuntimeException(e);
        }
    }
}
