package com.delay;

import com.delay.config.RabbitDelayConfig;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;

/**
 * @author Sparkler
 * @date 2022/3/2
 */
@SpringBootTest
public class RabbitDelayTest {
    @Autowired
    RabbitTemplate rabbitTemplate;

    @Test
    public void rabbitDelayTest() throws UnsupportedEncodingException {
        Message msg = MessageBuilder.withBody(("hello 这是一条来自 " + LocalDateTime.now() + " 的消息").getBytes("UTF-8")).setHeader("x-delay", 3000).build();
        rabbitTemplate.convertAndSend(RabbitDelayConfig.EXCHANGE_NAME, RabbitDelayConfig.QUEUE_NAME, msg);
    }
}
