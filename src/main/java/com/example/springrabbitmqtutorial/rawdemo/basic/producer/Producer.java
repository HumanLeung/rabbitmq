package com.example.springrabbitmqtutorial.rawdemo.basic.producer;

import com.example.springrabbitmqtutorial.rawdemo.basic.common.RabbitmqUtils;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

/**
 * @author Administrator
 */
public class Producer {
    public static final String QUEUE_NAME = "hello";

    public static void main(String[] args) {
        try ( Connection connection = RabbitmqUtils.getConnection();
              Channel channel = connection.createChannel();){
            channel.queueDeclare(QUEUE_NAME,false,false,false,null);
            String message = "hello world";
            channel.basicPublish("",QUEUE_NAME,null,message.getBytes(StandardCharsets.UTF_8));
            System.out.println("done!");
        } catch (IOException | TimeoutException e) {
            throw new RuntimeException(e);
        }

    }
}
