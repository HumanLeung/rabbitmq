package com.example.springrabbitmqtutorial.rawdemo.basic.workthread2;

import com.example.springrabbitmqtutorial.rawdemo.basic.common.RabbitmqUtils;
import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

/**
 * @author Administrator
 */
public class Worker03 {

    public static final String TASK_QUEUE_NAME = "45fd707c-4af8-46dc-8978-9684d36965dc";


    public static void main(String[] args) throws IOException, TimeoutException {

        CancelCallback cancelCallback = consumerTag -> System.out.println(consumerTag + "message interrupted");
            Channel channel = RabbitmqUtils.getChannel();
            DeliverCallback deliverCallback = (consumerTag, message) -> {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("received message: " + new String(message.getBody(), StandardCharsets.UTF_8));
                channel.basicAck(message.getEnvelope().getDeliveryTag(),false);
            };
            System.out.println("Worker03 is waiting........");
            channel.basicConsume(TASK_QUEUE_NAME,false,deliverCallback,cancelCallback);
    }
}
