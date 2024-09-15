package com.jwattsuk.sample.services.strategy;

import com.jwattsuk.sample.dao.JpaTradeRepository;
import com.jwattsuk.sample.model.Trade;

import java.util.List;
import java.util.Optional;

public class LocalStorageStrategy implements StorageStrategy {

    private final JpaTradeRepository repository;

    public LocalStorageStrategy(JpaTradeRepository repository) {
        this.repository = repository;
    }

    @Override
    public Optional<Trade> GetTrade(String id) {
        return repository.findById(id);
    }

    @Override
    public List<Trade> GetAllTrades() {
        return repository.findAll();
    }

    @Override
    public Trade SaveNewTrade(Trade trade) {
        return repository.save(trade);
    }

    @Override
    public Trade UpdateTrade(Trade newTrade, String id) {
        return repository.findById(id)
                .map(trade -> {
                    trade.setInstrument(newTrade.getInstrument());
                    trade.setCounterparty(newTrade.getCounterparty());
                    trade.setBuySell(newTrade.getBuySell());
                    trade.setNotional(newTrade.getNotional());
                    trade.setCurrency(newTrade.getCurrency());
                    return repository.save(trade);
                })
                .orElseGet(() -> {
                    return repository.save(newTrade);
                });
    }

    @Override
    public void DeleteTrade(String id) {
        repository.deleteById(id);
    }
}
