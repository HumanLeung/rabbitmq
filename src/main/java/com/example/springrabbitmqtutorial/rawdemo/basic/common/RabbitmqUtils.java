package com.example.springrabbitmqtutorial.rawdemo.basic.common;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author Administrator
 */
public class RabbitmqUtils {
    private RabbitmqUtils(){

    }
    public static Connection getConnection() throws IOException, TimeoutException {
        ConnectionFactory factor = new ConnectionFactory();
        factor.setHost("146.190.102.94");
        factor.setUsername("admin");
        factor.setPassword("qq123456");
        return factor.newConnection();
    }

    public static Channel getChannel() throws IOException, TimeoutException {
       return getConnection().createChannel();
    }
}
