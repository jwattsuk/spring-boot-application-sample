package com.jwattsuk.sample.services;

import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MessageListenerInitializer {
    private static final Logger LOG = LoggerFactory.getLogger(MessageListenerInitializer.class);

    private MessageListener messageListener;
    private int threads;

    public MessageListenerInitializer(int threads, MessageListener messageListener) {
        this.threads = threads;
        this.messageListener = messageListener;
    }

    @PostConstruct
    public void initialize() throws InterruptedException {
        for (int thread = 1; thread <= threads; thread++) {
            LOG.info("Starting Message Listener thread {}", thread);
            messageListener.processTransactions(thread);
        }
    }
}
