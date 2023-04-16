package com.example.springrabbitmqtutorial.rawdemo.basic.workthread;

import com.example.springrabbitmqtutorial.rawdemo.basic.common.RabbitmqUtils;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

import java.util.Scanner;

/**
 * @author Administrator
 */
public class MQTask {
    public static final String QUEUE_NAME = "hello";

    public static void main(String[] args) {
        try(Connection connection = RabbitmqUtils.getConnection();
            Channel channel = connection.createChannel();) {
            Scanner scanner = new Scanner(System.in);
            while (scanner.hasNext()){
                String message = scanner.next();
                channel.basicPublish("",QUEUE_NAME,null,message.getBytes());
                System.out.println("发送消息完成： "+message);
            }
        }catch (Exception e){
         e.printStackTrace();
        }
    }
}
