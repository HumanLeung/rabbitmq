package com.example.springrabbitmqtutorial.springdemo.config;

import com.rabbitmq.client.AMQP;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Administrator
 */
@Configuration
public class RabbitMQConfig {
    public static final String EXCHANGE_NAME = "fanout-order-exchange";
    public static final String TOPIC_EXCHANGE_NAME = "topic-order-exchange";
    public static final String TOPIC_SMS_QUEUE = "sms-topic-queue";
    public static final String TOPIC_EMAIL_QUEUE = "email-topic-queue";
    public static final String SMS_QUEUE = "sms-fanout-queue";
    public static final String EMAIL_QUEUE = "email-fanout-queue";
    public static final String WECHAT_QUEUE = "wechat-fanout-queue";

    @Bean
    public FanoutExchange fanoutExchange() {
        /*
         * FanoutExchange的参数说明:
         * 1. 交换机名称
         * 2. 是否持久化 true：持久化，交换机一直保留 false：不持久化，用完就删除
         * 3. 是否自动删除 false：不自动删除 true：自动删除
         */
        return new FanoutExchange(EXCHANGE_NAME,true,false);
    }

    @Bean
    public TopicExchange topicExchange(){
        /**
         * topicExchange的参数说明:
         * 1. 交换机名称
         * 2. 是否持久化 true：持久化，交换机一直保留 false：不持久化，用完就删除
         * 3. 是否自动删除 false：不自动删除 true：自动删除
         */
        return new TopicExchange(TOPIC_EXCHANGE_NAME, true, false);
    }

    @Bean
    public Queue topicQueue() {
       return new Queue(TOPIC_SMS_QUEUE,true);
    }
    @Bean
    public Queue topicEmailQueue() {
        return new Queue(TOPIC_EMAIL_QUEUE,true);
    }

    @Bean
    public Queue smsQueue(){
    /**
      * Queue构造函数参数说明
      * 1. 队列名
      * 2. 是否持久化 true：持久化 false：不持久化
     */
        return new Queue(SMS_QUEUE, true);
    }
    @Bean
    public Queue emailQueue() {
        return new Queue(EMAIL_QUEUE, true);
    }
    @Bean
    public Queue wechatQueue() {
        return new Queue(WECHAT_QUEUE, true);
    }

    @Bean
    public Binding smsTopicBinding(){
        return BindingBuilder.bind(topicQueue()).to(topicExchange()).with("*.sms.#");
    }
    @Bean
    public Binding emailTopicBinding(){
        return BindingBuilder.bind(topicEmailQueue()).to(topicExchange()).with("#.email.*");
    }

    @Bean
    public Binding smsBinding(){
        return BindingBuilder.bind(smsQueue()).to(fanoutExchange());
    }

    @Bean
    public Binding emailBinding(){
        return BindingBuilder.bind(emailQueue()).to(fanoutExchange());
    }
    @Bean
    public Binding wechatBinding(){
        return BindingBuilder.bind(wechatQueue()).to(fanoutExchange());
    }
}
