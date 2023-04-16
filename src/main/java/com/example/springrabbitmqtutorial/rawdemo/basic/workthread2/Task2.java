package com.example.springrabbitmqtutorial.rawdemo.basic.workthread2;

import com.example.springrabbitmqtutorial.rawdemo.basic.common.RabbitmqUtils;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ChannelContinuationTimeoutException;
import com.rabbitmq.client.MessageProperties;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;
import java.util.concurrent.TimeoutException;

/**
 * @author Administrator
 */
public class Task2 {
    public static final String TASK_QUEUE_NAME = "ack_queue";

    public static void main(String[] args) {
        try(Channel channel = RabbitmqUtils.getChannel();) {
            boolean durable = true;
            //声明队列
            channel.queueDeclare(TASK_QUEUE_NAME,durable,false,false,null);
            //从控制台中输入信息
            Scanner scanner = new Scanner(System.in);
            while(scanner.hasNext()){
                String message = scanner.nextLine();
                //设置并不公平分发
                channel.basicQos(1);
                //持久化消息
                channel.basicPublish("",TASK_QUEUE_NAME, MessageProperties.PERSISTENT_TEXT_PLAIN,message.getBytes(StandardCharsets.UTF_8));
                System.out.println("message: "+message);
            }
        } catch (IOException | TimeoutException e) {
            throw new RuntimeException(e);
        }
    }
}
