package com.jwattsuk.sample.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MessageProcessor {
    private static final Logger LOG = LoggerFactory.getLogger(MessageProcessor.class);

    public void processMessage() throws InterruptedException {
        LOG.info("Processing Message, will wait for 30 seconds...");
        Thread.sleep(30000);  // Pretend this is a blocking thread doing something useful such as polling a DB
    }
}
