package com.basic.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * FanoutExchange 的数据交换策略是把所有到达 FanoutExchange 的消息转发给所有与它绑定的 Queue 上，在这种策略中，routingkey 将不起任何作用
 */
@Configuration
public class RabbitFanoutConfig {
    public static final String QUEUE_NAME_1 = "fanout_queue_1";
    public static final String QUEUE_NAME_2 = "fanout_queue_2";
    public static final String EXCHANGE_NAME = "fanout_exchange";

    @Bean
    FanoutExchange fanoutExchange() {
        return new FanoutExchange(EXCHANGE_NAME, true, false);
    }

    @Bean
    Queue queueOne() {
        return new Queue(QUEUE_NAME_1);
    }

    @Bean
    Queue queueTwo() {
        return new Queue(QUEUE_NAME_2);
    }

    @Bean
    Binding bindingOne() {
        return BindingBuilder.bind(queueOne()).to(fanoutExchange());
    }

    @Bean
    Binding bindingTwo() {
        return BindingBuilder.bind(queueTwo()).to(fanoutExchange());
    }
}
