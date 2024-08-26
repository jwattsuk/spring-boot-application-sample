package com.jwattsuk.sample.config;

import com.jwattsuk.sample.SampleProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.embedded.jetty.JettyServletWebServerFactory;
import org.springframework.boot.web.servlet.server.ServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ProcessorConfiguration {
    private static final Logger LOG = LoggerFactory.getLogger(ProcessorConfiguration.class);

    @Bean
    public SampleProcessor sampleProcessor() {
        LOG.info("SETTING UP SAMPLE PROCESSOR");
        return new SampleProcessor();
    }

}
