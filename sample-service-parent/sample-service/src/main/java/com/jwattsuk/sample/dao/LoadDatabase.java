package com.jwattsuk.sample.dao;

import com.jwattsuk.sample.model.Trade;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LoadDatabase {

    private static final Logger LOG = LoggerFactory.getLogger(LoadDatabase.class);

    @Bean
    CommandLineRunner initDatabase(TradeRepository repository) {
        return args -> {
            LOG.info("Preloading " +
                    repository.save(
                            new Trade("SWAP",
                                    "GOLDMANSACHS",
                                    Long.valueOf(100000),
                                    "BUY",
                                    "USD")));
            LOG.info("Preloading " +
                    repository.save(
                            new Trade("SWAP",
                                    "JPMORGAN",
                                    Long.valueOf(100000),
                                    "SELL",
                                    "GBP")));
        };
    }
}
