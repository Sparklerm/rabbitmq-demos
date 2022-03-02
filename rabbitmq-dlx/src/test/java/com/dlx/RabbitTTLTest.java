package com.dlx;

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
public class RabbitTTLTest {
    @Autowired
    RabbitTemplate rabbitTemplate;

    /**
     * 方式一，在发送消息是声明消息的有效期。
     */
    @Test
    void method1() {
        // 时间单位为毫秒
        Message message = MessageBuilder.withBody("hello ttl queue demo1".getBytes())
                .setExpiration("10000")
                .build();
        rabbitTemplate.convertAndSend(RabbitTTLConfig.QUEUE_NAME_1, message);
    }

    /**
     * 方式一，在发送消息是声明消息的有效期。
     */
    @Test
    void method2() {
        // 时间单位为毫秒
        Message message = MessageBuilder.withBody("hello ttl queue demo2".getBytes())
                .build();
        rabbitTemplate.convertAndSend(RabbitTTLConfig.QUEUE_NAME_2, message);
    }
}
