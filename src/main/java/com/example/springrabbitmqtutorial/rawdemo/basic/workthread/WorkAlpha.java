package com.example.springrabbitmqtutorial.rawdemo.basic.workthread;

import com.example.springrabbitmqtutorial.rawdemo.basic.common.RabbitmqUtils;
import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.DeliverCallback;

/**
 * @author Administrator
 */
public class WorkAlpha {
    public static final String QUEUE_NAME = "hello";

    public static void main(String[] args) {
        DeliverCallback deliverCallback = (consumerTag,message) -> System.out.println(new String(message.getBody()));
        CancelCallback cancelCallback = consumerTag -> System.out.println(consumerTag + "message interrupted");
        try(Connection connection = RabbitmqUtils.getConnection();
            Channel channel = connection.createChannel();){
            System.out.println("C1 is waiting for message........");
            channel.basicConsume(QUEUE_NAME,true,deliverCallback,cancelCallback);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
