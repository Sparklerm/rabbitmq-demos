package com.delay;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author Sparkler
 * @date 2022/3/2
 */
@SpringBootApplication
public class RabbitDelayApplication {
    public static void main(String[] args) {
        SpringApplication.run(RabbitDelayApplication.class, args);
    }
}
