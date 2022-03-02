package com.dlx;

import com.dlx.config.RabbitDLXConfig;
import com.dlx.config.RabbitTTLConfig;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author Sparkler
 * @date 2022/3/2
 */
@SpringBootTest
public class RabbitDLXTest {
    @Autowired
    RabbitTemplate rabbitTemplate;

    @Test
    public void send() {
        Message message = MessageBuilder.withBody("This is dlx exchange test message ! ".getBytes())
                .build();
        rabbitTemplate.convertAndSend(RabbitDLXConfig.TTL_QUEUE_NAME, message);
    }
}
