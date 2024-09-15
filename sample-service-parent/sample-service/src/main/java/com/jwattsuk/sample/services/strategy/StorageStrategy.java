package com.jwattsuk.sample.services.strategy;

import com.jwattsuk.sample.model.Trade;

import java.util.List;
import java.util.Optional;

public interface StorageStrategy {
    Optional<Trade> GetTrade(String id);
    List<Trade> GetAllTrades();
    Trade SaveNewTrade(Trade trade);
    Trade UpdateTrade(Trade newTrade, String id);
    void DeleteTrade(String id);
}
