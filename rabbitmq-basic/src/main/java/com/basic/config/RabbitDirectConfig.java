package com.basic.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * DirectExchange 的路由策略是将消息队列绑定到一个 DirectExchange 上，
 * 当一条消息到达 DirectExchange 时会被转发到与该条消息 routing key 相同的 Queue 上，
 * 例如消息队列名为 “hello-queue”，则 routingkey 为 “hello-queue”
 */
@Configuration
public class RabbitDirectConfig {
    public static final String DIRECT_NAME = "direct_exchange";
    public static final String QUEUE_NAME = "direct_queue";
    public static final String ROUTING_KEY = "DIRECT";

    /**
     * 创建队列
     *
     * @return
     */
    @Bean
    Queue queue() {
        return new Queue(QUEUE_NAME);
    }

    /**
     * 创建交换机
     *
     * @return
     */
    @Bean
    DirectExchange directExchange() {
        return new DirectExchange(DIRECT_NAME, true, false);
    }

    /**
     * 队列交换交换机
     *
     * @return
     */
    @Bean
    Binding binding() {
        return BindingBuilder.bind(queue()).to(directExchange()).with(ROUTING_KEY);
    }
}
