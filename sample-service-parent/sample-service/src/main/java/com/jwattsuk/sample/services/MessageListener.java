package com.jwattsuk.sample.services;

import org.springframework.scheduling.annotation.Async;

public class MessageListener {

    private final MessageProcessor messageProcessor;

    public MessageListener(MessageProcessor messageProcessor) {
        this.messageProcessor = messageProcessor;
    }

    @Async
    public void processTransactions(final int threadNumber) throws InterruptedException {
        Thread.currentThread().setName("PublisherThread-" + threadNumber);
        do {
            messageProcessor.processMessage();
        } while (threadNumber > 0);

    }
}
