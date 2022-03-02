package com.basic;

import com.basic.config.RabbitTopicConfig;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class RabbitTopicTest {
    @Autowired
    RabbitTemplate rabbitTemplate;

    @Test
    void contextLoads() {
        rabbitTemplate.convertAndSend(RabbitTopicConfig.TOPIC_EXCHANGE, RabbitTopicConfig.COLOR_BLUE + ".son", "Cambridge blue");
        rabbitTemplate.convertAndSend(RabbitTopicConfig.TOPIC_EXCHANGE, RabbitTopicConfig.COLOR_RED + ".son", "Pink");
        rabbitTemplate.convertAndSend(RabbitTopicConfig.TOPIC_EXCHANGE, RabbitTopicConfig.COLOR_BLUE + ".color", "蓝色");
        rabbitTemplate.convertAndSend(RabbitTopicConfig.TOPIC_EXCHANGE, RabbitTopicConfig.COLOR_RED + ".color", "红色");
        rabbitTemplate.convertAndSend(RabbitTopicConfig.TOPIC_EXCHANGE, "phone.desc", "颜色");
    }
}
