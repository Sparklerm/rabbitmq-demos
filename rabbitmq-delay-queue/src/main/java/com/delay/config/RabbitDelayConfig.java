package com.delay.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.CustomExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Sparkler
 * @date 2022/3/2
 */
@Configuration
public class RabbitDelayConfig {
    public static final String QUEUE_NAME = "delay_queue";
    public static final String EXCHANGE_NAME = "delay_exchange";
    public static final String EXCHANGE_TYPE = "x-delayed-message";

    @Bean
    Queue queue() {
        return new Queue(QUEUE_NAME, true, false, false);
    }

    /**
     * 这里我们使用的交换机是 CustomExchange，这是一个 Spring 中提供的交换机，创建 CustomExchange 时有五个参数，含义分别如下：
     * 交换机名称。
     * 交换机类型，这个地方是固定的。
     * 交换机是否持久化。
     * 如果没有队列绑定到交换机，交换机是否删除。
     * 其他参数。
     * 最后一个 args 参数中，指定了交换机消息分发的类型，这个类型就是 direct、fanout、topic 以及 header 几种，用了哪种类型，将来交换机分发消息就按哪种方式来。
     *
     * @return
     */
    @Bean
    CustomExchange customExchange() {
        Map<String, Object> args = new HashMap<>();
        args.put("x-delayed-type", "direct");
        return new CustomExchange(EXCHANGE_NAME, EXCHANGE_TYPE, true, false, args);
    }

    @Bean
    Binding binding() {
        return BindingBuilder.bind(queue())
                .to(customExchange()).with(QUEUE_NAME).noargs();
    }
}
