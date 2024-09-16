package com.jwattsuk.sample.config;

import com.jwattsuk.sample.services.MessageListener;
import com.jwattsuk.sample.services.MessageListenerInitializer;
import com.jwattsuk.sample.services.MessageProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;

import java.util.concurrent.Executor;

@Configuration
@EnableAsync
public class MessageListenerConfiguration implements AsyncConfigurer {
    private static final Logger LOG = LoggerFactory.getLogger(MessageListenerConfiguration.class);

    @Autowired
    private ApplicationContext applicationContext;

    @Value("${threadPoolSize}")
    private int threadPoolSize;

    @Value("${threadCrashLimit}")
    private int threadCrashLimit;

    @Bean
    public MessageListener messageListener(final MessageProcessor messageProcessor) {
        LOG.info("SETTING UP MESSAGE LISTENER");
        return new MessageListener(messageProcessor);
    }

    @Bean
    public MessageListenerInitializer messageListenerInitializer(final MessageListener messageListener) {
        LOG.info("SETTING UP MESSAGE LISTENER INITIALIZER");
        return new MessageListenerInitializer(threadPoolSize, messageListener);
    }

    @Bean
    public MessageProcessor messageProcessor() {
        LOG.info("SETTING UP MESSAGE PROCESSOR");
        return new MessageProcessor();  // Probably would take a DAO parameter
    }

    @Override
    public Executor getAsyncExecutor() {
        return new SimpleAsyncTaskExecutor();
    }

    @Override
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
        LOG.info("SETTING UP ASYNC EXCEPTION HANDLER");
        LOG.info("Setting crash limit to {}", threadCrashLimit);
        return new CustomAsyncExceptionHandler(applicationContext, threadCrashLimit);
    }

}
