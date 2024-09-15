package com.jwattsuk.sample.config;

import com.jwattsuk.sample.dao.JpaTradeRepository;
import com.jwattsuk.sample.model.Trade;
import com.jwattsuk.sample.services.strategy.LocalStorageStrategy;
import com.jwattsuk.sample.services.strategy.StorageStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("local")
public class LocalDbConfiguration {

    private static final Logger LOG = LoggerFactory.getLogger(LocalDbConfiguration.class);

    @Bean
    CommandLineRunner initDatabase(JpaTradeRepository jpaTradeRepository) {
        return args -> {
            LOG.info("Preloading " +
                    jpaTradeRepository.save(
                            new Trade("SWAP",
                                    "GOLDMANSACHS",
                                    Long.valueOf(100000),
                                    "BUY",
                                    "USD")));
            LOG.info("Preloading " +
                    jpaTradeRepository.save(
                            new Trade("SWAP",
                                    "JPMORGAN",
                                    Long.valueOf(100000),
                                    "SELL",
                                    "GBP")));
        };
    }

    @Bean
    StorageStrategy localStorageStrategy(JpaTradeRepository repository) {
        return new LocalStorageStrategy(repository);
    }
}
