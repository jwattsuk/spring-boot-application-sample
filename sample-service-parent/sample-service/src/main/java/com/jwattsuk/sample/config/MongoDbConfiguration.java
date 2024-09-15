package com.jwattsuk.sample.config;

import com.jwattsuk.sample.dao.MongoDBTradeDao;
import com.jwattsuk.sample.services.strategy.MongoStorageStrategy;
import com.jwattsuk.sample.services.strategy.StorageStrategy;
import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.MongoCredential;
import com.mongodb.client.MongoClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Profile;
import org.springframework.data.mongodb.core.MongoTemplate;
import com.mongodb.client.MongoClients;

import java.util.concurrent.TimeUnit;


/*
  Derived from https://docs.spring.io/spring-data/mongodb/reference/mongodb/configuration.html
*/
@Configuration
@EnableConfigurationProperties(MongoDbProperties.class)
@Profile("!local")
public class MongoDbConfiguration {
    @Autowired
    private MongoDbProperties mongoDbProperties;

    private static final Logger LOG = LoggerFactory.getLogger(MongoDbConfiguration.class);

    @Bean
    MongoClient mongoClient() {
        LOG.info("SETTING UP MONGODB CLIENT");
        ConnectionString connectionString
                = new ConnectionString(mongoDbProperties.getConnectionString());
        MongoClientSettings mongoClientSettings = MongoClientSettings.builder()
                .credential(getMongoCredentials())
                .applyConnectionString(connectionString)
                .applyToConnectionPoolSettings(builder -> builder.maxSize(20)
                        .minSize(2)
                        .maxWaitTime(2000, TimeUnit.MILLISECONDS))
                .build();
        return MongoClients.create(mongoClientSettings);
    }

    private MongoCredential getMongoCredentials() {
        return MongoCredential.createCredential(
                mongoDbProperties.getUser(),
                mongoDbProperties.getDatabaseName(),
                mongoDbProperties.getPassword().toCharArray());
    }

    @Bean
    MongoTemplate mongoTemplate(MongoClient mongoClient) {
        LOG.info("SETTING UP MONGODB TEMPLATE");
        return new MongoTemplate(mongoClient, mongoDbProperties.getDatabaseName());
    }

    @Bean
    public MongoDBTradeDao tradeDao(MongoTemplate template) {
        LOG.info("SETTING UP TRADE DAO");
        return new MongoDBTradeDao(template);
    }

    @Bean
    public StorageStrategy mongoStorageStrategy(MongoDBTradeDao mongoDBTradeDao) {
        return new MongoStorageStrategy(mongoDBTradeDao);
    }
}
