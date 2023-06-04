package com.example.springrabbitmqtutorial.web;

import com.example.springrabbitmqtutorial.streamproducer.producer.MessageProducer01;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/test")
@RestController
public class MQController {

    @Autowired
    MessageProducer01 messageProducer01;

    @GetMapping("/t1")
    public void sendMsg(){
      messageProducer01.send("hello body");
    }
}
