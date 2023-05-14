package com.example.springrabbitmqtutorial.rawdemo.basic.workthread3;

import com.example.springrabbitmqtutorial.rawdemo.basic.common.RabbitmqUtils;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConfirmCallback;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.UUID;
import java.util.concurrent.ConcurrentNavigableMap;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.TimeoutException;

/**
 * 1、单个确认
 * 2、批量确认
 * 3、异步批量确认
 * @author Administrator
 */
public class ConfirmMessage {

    public static final int MESSAGE_COUNT = 1000;

    public static void main(String[] args) throws IOException, TimeoutException, InterruptedException {
//      publicMessageIndividually();
//        publishMessageBatch();
        publicMessageAsync();
    }

    public static void publicMessageIndividually() throws IOException, TimeoutException, InterruptedException {
        Channel channel = RabbitmqUtils.getChannel();
        String queueName = UUID.randomUUID().toString();
        channel.queueDeclare(queueName,true,false,false,null);
        //开启发布确认
        channel.confirmSelect();
        //开启时间
        long begin = System.currentTimeMillis();

        for (int i = 0; i < MESSAGE_COUNT; i++){
            String message = i + "";
            channel.basicPublish("",queueName,null,message.getBytes());

            boolean flag = channel.waitForConfirms();

            if (flag){
                System.out.println("消息发送成功");
            }
        }

        long end = System.currentTimeMillis();
        System.out.println("发布" + MESSAGE_COUNT + "个单独确认消息，耗时"+ (end - begin) + "ms");
    }

    public static void publishMessageBatch() throws IOException, TimeoutException, InterruptedException {
        Channel channel = RabbitmqUtils.getChannel();
        String queueName = UUID.randomUUID().toString();
        channel.queueDeclare(queueName,true,false,false,null);
        //开启发布确认
        channel.confirmSelect();
        //开启时间
        long begin = System.currentTimeMillis();

        //批量确认消息大小
        int batchSize = 100;
        //未确认消息个数
        int outstandingMessageCount = 0;
        //批量发送消息 批量发布确认
        for(int i = 0; i < MESSAGE_COUNT; i++){
            String message = i + "";
            channel.basicPublish("",queueName,null,message.getBytes(StandardCharsets.UTF_8));
            outstandingMessageCount++;
            //判断达到100条消息的时候批量确认一次
            if (outstandingMessageCount == batchSize){
                channel.waitForConfirms();
                outstandingMessageCount = 0;
            }
        }

        if (outstandingMessageCount > 0){
            channel.waitForConfirms();
        }
        //结束时间
        long end = System.currentTimeMillis();
        System.out.println("发布" + MESSAGE_COUNT + "个批量确认消息，耗时" + (end - begin) + "ms");
    }

    public static void publicMessageAsync() throws IOException, TimeoutException {
        Channel channel = RabbitmqUtils.getChannel();

        String queueName = UUID.randomUUID().toString();
        channel.queueDeclare(queueName,true,false,false,null);
        channel.confirmSelect();
        long begin = System.currentTimeMillis();

        ConcurrentSkipListMap<Long,String> outstandingConfirms = new ConcurrentSkipListMap<>();

        //消息确认成功 回调函数
        ConfirmCallback ackCallback = (deliveryTag, multiple) -> {
            if (multiple){
                ConcurrentNavigableMap<Long,String> confirmed =
                        outstandingConfirms.headMap(deliveryTag);
                confirmed.clear();
            }else{
                outstandingConfirms.remove(deliveryTag);
            }
            System.out.println("确认的消息：" + deliveryTag);
        };

        ConfirmCallback nackCallback = (deliveryTag, multiple) -> {
            String message = outstandingConfirms.get(deliveryTag);
            System.out.println("未确认消息是："+message+"未确认的消息："+deliveryTag);
        };

        channel.addConfirmListener(ackCallback,nackCallback);

        for (int i = 0; i < MESSAGE_COUNT; i++){
            String message = "消息" + i;
            channel.basicPublish("",queueName,null,message.getBytes(StandardCharsets.UTF_8));
            // 1:此处记录下所有要发送的消息 消息的总和
            outstandingConfirms.put(channel.getNextPublishSeqNo(),message);
        }

        long end = System.currentTimeMillis();
        System.out.println("发布"+ MESSAGE_COUNT +"个异步发布确认消息，消耗"+(end - begin)+"ms");

    }
}
