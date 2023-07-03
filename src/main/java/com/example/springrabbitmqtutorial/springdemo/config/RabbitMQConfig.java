package com.example.springrabbitmqtutorial.springdemo.config;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

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
    public static final String ORDER_EXCHANGE = "order_exchange";
    public static final String ORDER_QUEUE = "order_queue";
    public static final String ORDER_QUEUE_ROUTING_KEY = "order.#";
    public static final String ORDER_DEAD_LETTER_EXCHANGE = "order_dead_letter_exchange";
    public static final String ORDER_DEAD_LETTER_QUEUE_ROUTING_KEY = "order_dead_letter_queue_routing_key";
    public static final String ORDER_DEAD_LETTER_QUEUE = "order_dead_letter_queue";

    @Bean("orderDeadLetterExchange")
    public Exchange orderDeadLetterExchange() {
        return new TopicExchange(ORDER_DEAD_LETTER_EXCHANGE,true,false);
    }

    @Bean("orderDeadLetterQueue")
    public Queue orderDeadLetterQueue() {
        return QueueBuilder.durable(ORDER_DEAD_LETTER_QUEUE).build();
    }


    @Bean("orderDeadLetterBinding")
    public Binding orderDeadLetterBinding(@Qualifier("orderDeadLetterQueue") Queue queue,
                                          @Qualifier("orderDeadLetterExchange")Exchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(ORDER_DEAD_LETTER_QUEUE_ROUTING_KEY).noargs();
    }

    /**
     * 创建订单交换机
     */
    @Bean("orderExchange")
    public Exchange orderExchange() {
        return new TopicExchange(ORDER_EXCHANGE, true, false);
    }

    @Bean("orderQueue")
    public Queue orderQueue(){
        Map<String,Object> args = new HashMap<>();

        //消息过期后，进入到死信交换机
        args.put("x-dead-letter-exchange", ORDER_DEAD_LETTER_EXCHANGE);

        //消息过期后，进入到死信交换机的路由key
        args.put("x-dead-letter-routing-key", ORDER_DEAD_LETTER_QUEUE_ROUTING_KEY);

        //过期时间，单位毫秒
        args.put("x-message-ttl", 10000);
        return QueueBuilder.durable(ORDER_QUEUE).withArguments(args).build();
    }

    /**
     * 绑定订单交换机和队列
     */
    @Bean("orderBinding")
    public Binding orderBinding(@Qualifier("orderQueue") Queue queue,
                                @Qualifier("orderExchange")Exchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(ORDER_QUEUE_ROUTING_KEY).noargs();
    }

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
