package com.example.springrabbitmqtutorial.springdemo.callback;

import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.ReturnedMessage;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * @author Administrator
 */
@Component
@Slf4j
@Scope("prototype")
public class OrderCallback implements RabbitTemplate.ReturnsCallback, RabbitTemplate.ConfirmCallback{


    @Override
    public void confirm(CorrelationData correlationData, boolean ack, String cause) {
        log.info("回调数据：{}", correlationData);
        log.info("确认结果：{}", ack);
        log.info("返回原因：{}", cause);
    }

    @Override
    public void returnedMessage(ReturnedMessage returned) {
        log.info("发送消息：{}", JSONUtil.toJsonStr(returned.getMessage()));
        log.info("结果状态码：{}", returned.getReplyCode());
        log.info("结果状态信息：{}", returned.getReplyText());
        log.info("交换机：{}", returned.getExchange());
        log.info("路由key：{}", returned.getRoutingKey());
    }
}
