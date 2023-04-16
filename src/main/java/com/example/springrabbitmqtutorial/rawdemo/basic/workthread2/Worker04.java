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
public class Worker04 {

    public static final String TASK_QUEUE_NAME = "ack_queue";


    public static void main(String[] args) throws IOException, TimeoutException {

        CancelCallback cancelCallback = consumerTag -> System.out.println(consumerTag + "message interrupted");
        Channel channel = RabbitmqUtils.getChannel();
            System.out.println("Worker04 is waiting........");
            DeliverCallback deliverCallback = (consumerTag, message) -> {
                try {
                    Thread.sleep(30000);
                    System.out.println("received message: " + new String(message.getBody(), StandardCharsets.UTF_8));
                    channel.basicAck(message.getEnvelope().getDeliveryTag(),false);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            };

            channel.basicConsume(TASK_QUEUE_NAME,false,deliverCallback,cancelCallback);
    }
}
