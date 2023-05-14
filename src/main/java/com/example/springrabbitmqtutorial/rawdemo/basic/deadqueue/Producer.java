package com.example.springrabbitmqtutorial.rawdemo.basic.deadqueue;

import com.example.springrabbitmqtutorial.rawdemo.basic.common.RabbitmqUtils;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;

/**
 * @author Administrator
 */
public class Producer {
    private static final String NORMAL_EXCHANGE = "normal_exchange";

    public static void main(String[] args) {
        test2();
    }
    public static void test(){
        try(Channel channel = RabbitmqUtils.getChannel()){
            channel.exchangeDeclare(NORMAL_EXCHANGE, BuiltinExchangeType.DIRECT);
            AMQP.BasicProperties properties = new AMQP.BasicProperties().builder().expiration("10000").build();
            for (int i = 1; i < 11; i++){
                String message="info"+i;
                channel.basicPublish(NORMAL_EXCHANGE,"Ken",properties,message.getBytes());
                System.out.println("生产者发送消息："+message);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public static void test2(){
        try(Channel channel = RabbitmqUtils.getChannel()){
            channel.exchangeDeclare(NORMAL_EXCHANGE, BuiltinExchangeType.DIRECT);
            for (int i = 1; i < 11; i++){
                String message="info"+i;
                channel.basicPublish(NORMAL_EXCHANGE,"Ken",null,message.getBytes());
                System.out.println("生产者发送消息："+message);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
