package com.example.springrabbitmqtutorial.rawdemo.basic.exchange.direct;

import com.example.springrabbitmqtutorial.rawdemo.basic.common.RabbitmqUtils;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.TimeoutException;

/**
 * @author Administrator
 */
public class DirectLogs {
    public static final String EXCHANGE_NAME = "direct_logs";

    public static void main(String[] args) throws IOException, TimeoutException {
        Channel channel = RabbitmqUtils.getChannel();
        Scanner scanner = new Scanner(System.in);
        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.DIRECT);

        Map<String,String> bindingKeyMap = new HashMap<>();
        bindingKeyMap.put("info","normal info message");
        bindingKeyMap.put("warning","warn message");
        bindingKeyMap.put("error","error message");
        bindingKeyMap.put("debug","debug message");
        for (Map.Entry<String, String> bindingKeyEntry: bindingKeyMap.entrySet()){
             String bindingKey = bindingKeyEntry.getKey();
             String message = bindingKeyEntry.getValue();
             channel.basicPublish(EXCHANGE_NAME,bindingKey,null,message.getBytes(StandardCharsets.UTF_8));
            System.out.println("生产者发出消息:" + message);
        }

//        while (scanner.hasNext()){
//            String message = scanner.next();
//            channel.basicPublish(EXCHANGE_NAME,"error",null,message.getBytes(StandardCharsets.UTF_8));
//            System.out.println("生产者发出消息："+message);
//        }
    }
}
