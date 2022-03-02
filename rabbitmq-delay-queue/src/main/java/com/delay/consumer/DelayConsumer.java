package com.delay.consumer;

import com.delay.config.RabbitDelayConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * @author Sparkler
 * @date 2022/3/2
 */
@Component
public class DelayConsumer {
    private static final Logger logger = LoggerFactory.getLogger(DelayConsumer.class);

    @RabbitListener(queues = RabbitDelayConfig.QUEUE_NAME)
    public void delayConsumer(String message) {
        logger.info("Now time : {} ,Delay queue message ---> {}", LocalDateTime.now().toString(), message);
    }
}
