package com.project.ecommerce.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@Configuration
public class AsynConfig {

    @Bean(name = "taskExecutor")
    public Executor taskExecutor(){

        ThreadPoolTaskExecutor threadPoolTaskExecutor = new ThreadPoolTaskExecutor();
        threadPoolTaskExecutor.setCorePoolSize(4);
        threadPoolTaskExecutor.setQueueCapacity(20);
        threadPoolTaskExecutor.setMaxPoolSize(10);
        threadPoolTaskExecutor.setThreadNamePrefix("Email-Thread-");
        threadPoolTaskExecutor.initialize();

        return threadPoolTaskExecutor;
    }
}
