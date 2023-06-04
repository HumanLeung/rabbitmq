package com.example.springrabbitmqtutorial.streamproducer.producer;

import com.example.springrabbitmqtutorial.streamproducer.channel.MySource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

@Component
@EnableBinding(MySource.class)
public class MessageProducer01 {

    @Autowired
    private MySource mySource;

    public void send(String message){
        mySource.myOutput().send(MessageBuilder.withPayload(message).build());
    }
}
