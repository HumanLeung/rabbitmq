package com.example.springrabbitmqtutorial.streamconsumer;

import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.stereotype.Component;

@Component
@EnableBinding(MySink.class)
public class MessageConsumer01 {

    @StreamListener(MySink.MY_INPUT)
    public void receive(String message){
        System.out.println(message);
    }
}
