package com.jwattsuk.sample.config;

import com.jwattsuk.sample.services.TradeActionService;
import com.jwattsuk.sample.services.strategy.StorageStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfiguration {
    private static final Logger LOG = LoggerFactory.getLogger(AppConfiguration.class);

    @Bean
    public TradeActionService sampleProcessor(StorageStrategy storageStrategy) {
        LOG.info("SETTING UP TRADE ACTION SERVICE");
        return new TradeActionService(storageStrategy);
    }

}
