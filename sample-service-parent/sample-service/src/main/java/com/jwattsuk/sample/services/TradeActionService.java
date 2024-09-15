package com.jwattsuk.sample.services;

import com.jwattsuk.sample.model.Trade;
import com.jwattsuk.sample.services.strategy.StorageStrategy;

import java.util.List;
import java.util.Optional;

public class TradeActionService {

    private final StorageStrategy storageStrategy;

    public TradeActionService(StorageStrategy storageStrategy) {
        this.storageStrategy = storageStrategy;
    }

    public Optional<Trade> GetTrade(String id) {
        return storageStrategy.GetTrade(id);
    }

    public List<Trade> GetAllTrades() {
        return storageStrategy.GetAllTrades();
    }

    public Trade SaveNewTrade(Trade trade) {
        return storageStrategy.SaveNewTrade(trade);
    }

    public Trade UpdateTrade(Trade newTrade, String id) {
        return storageStrategy.UpdateTrade(newTrade, id);
    }

    public void DeleteTrade(String id) {
        storageStrategy.DeleteTrade(id);
    }
}
