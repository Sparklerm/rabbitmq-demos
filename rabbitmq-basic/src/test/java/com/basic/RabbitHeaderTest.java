package com.basic;

import com.basic.config.RabbitHeaderConfig;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class RabbitHeaderTest {
    @Autowired
    RabbitTemplate rabbitTemplate;

    @Test
    void contextLoads() {
        Message nameMsg = MessageBuilder.withBody("hello header! name-queue".getBytes()).setHeader("name", "sang").build();
        Message ageMsg = MessageBuilder.withBody("hello header! age-queue".getBytes()).setHeader("age", "99").build();
        rabbitTemplate.send(RabbitHeaderConfig.HEADER_EXCHANGE, null, ageMsg);
        rabbitTemplate.send(RabbitHeaderConfig.HEADER_EXCHANGE, null, nameMsg);
    }
}
