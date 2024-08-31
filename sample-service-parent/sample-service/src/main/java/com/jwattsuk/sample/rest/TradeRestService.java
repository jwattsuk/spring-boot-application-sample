package com.jwattsuk.sample.rest;

import com.jwattsuk.sample.SampleProcessor;
import com.jwattsuk.sample.dao.TradeRepository;
import com.jwattsuk.sample.model.Trade;
import com.jwattsuk.sample.exception.TradeNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class TradeRestService {

    private final SampleProcessor sampleProcessor;

    private final TradeRepository repository;

    public TradeRestService(SampleProcessor sampleProcessor, TradeRepository repository) {
        this.sampleProcessor = sampleProcessor;
        this.repository = repository;
    }

    @GetMapping("/trades")
    List<Trade> all() {
        return repository.findAll();
    }

    @PostMapping("/trades")
    Trade newTrade(@RequestBody Trade newTrade) {
        return repository.save(newTrade);
    }

    @GetMapping(value = "/trades/{id}")
    public Trade one(@PathVariable Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new TradeNotFoundException(id));
    }

    @PutMapping("/trades/{id}")
    Trade replaceTrade(@RequestBody Trade newTrade, @PathVariable Long id) {

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

    @DeleteMapping("/trades/{id}")
    void deleteTrade(@PathVariable Long id) {
        repository.deleteById(id);
    }
}
