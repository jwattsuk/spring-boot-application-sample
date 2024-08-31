package com.jwattsuk.sample;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication
public class SampleServiceApplication {
    private static final Logger LOG = LoggerFactory.getLogger(SampleServiceApplication.class);

    public static void main(String[] args) throws Exception {
        LOG.info("Starting Sample Service with process id {}", System.getProperty("PID"));
        SpringApplication.run(SampleServiceApplication.class, args);
    }
}