package com.basic;

import com.basic.config.RabbitFanoutConfig;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class RabbitFanoutTest {
    @Autowired
    RabbitTemplate rabbitTemplate;

    @Test
    void contextLoads() {
        for (int i = 1; i <= 20; i++) {
            rabbitTemplate.convertAndSend(RabbitFanoutConfig.EXCHANGE_NAME, null, "fanout msg " + i);
        }
    }
}
