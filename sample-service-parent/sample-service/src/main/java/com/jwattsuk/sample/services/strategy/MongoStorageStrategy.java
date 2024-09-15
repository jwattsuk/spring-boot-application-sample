package com.jwattsuk.sample.services.strategy;

import com.jwattsuk.sample.dao.MongoDBTradeDao;
import com.jwattsuk.sample.model.Trade;

import java.util.List;
import java.util.Optional;

public class MongoStorageStrategy implements StorageStrategy {

    private final MongoDBTradeDao mongoDBTradeDao;

    public MongoStorageStrategy(MongoDBTradeDao mongoDBTradeDao) {
        this.mongoDBTradeDao = mongoDBTradeDao;
    }

    @Override
    public Optional<Trade> GetTrade(String id) {
        return Optional.ofNullable(mongoDBTradeDao.getById(id));
    }

    @Override
    public List<Trade> GetAllTrades() {
        return mongoDBTradeDao.getAll();
    }

    @Override
    public Trade SaveNewTrade(Trade trade) {
        return mongoDBTradeDao.add(trade);
    }

    @Override
    public Trade UpdateTrade(Trade newTrade, String id) {
        return mongoDBTradeDao.update(newTrade);
    }

    @Override
    public void DeleteTrade(String id) {
        mongoDBTradeDao.delete(id);
    }
}
