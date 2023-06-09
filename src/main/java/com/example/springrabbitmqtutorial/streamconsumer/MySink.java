package com.example.springrabbitmqtutorial.streamconsumer;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.messaging.SubscribableChannel;

public interface MySink {
    String MY_INPUT = "my_input";

    @Input(MY_INPUT)
    SubscribableChannel input();
}
