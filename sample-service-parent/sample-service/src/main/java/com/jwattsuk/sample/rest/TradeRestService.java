package com.jwattsuk.sample.rest;

import com.jwattsuk.sample.services.TradeActionService;
import com.jwattsuk.sample.dao.JpaTradeRepository;
import com.jwattsuk.sample.model.Trade;
import com.jwattsuk.sample.exception.TradeNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class TradeRestService {

    private final TradeActionService tradeActionService;

    //private final JpaTradeRepository repository;

    public TradeRestService(TradeActionService tradeActionService, JpaTradeRepository repository) {
        this.tradeActionService = tradeActionService;
        //this.repository = repository;
    }

    @GetMapping("/trades")
    List<Trade> all() {
        return tradeActionService.GetAllTrades();
        //return repository.findAll();
    }

    @PostMapping("/trades")
    Trade newTrade(@RequestBody Trade newTrade) {
        return tradeActionService.SaveNewTrade(newTrade);
        //return repository.save(newTrade);
    }

    @GetMapping(value = "/trades/{id}")
    public Trade one(@PathVariable String id) {
        return tradeActionService.GetTrade(id)
                .orElseThrow(() -> new TradeNotFoundException(id));
//        return repository.findById(id)
//                .orElseThrow(() -> new TradeNotFoundException(id));
    }

    @PutMapping("/trades/{id}")
    Trade replaceTrade(@RequestBody Trade newTrade, @PathVariable String id) {
        return tradeActionService.UpdateTrade(newTrade, id);
//        return repository.findById(id)
//                .map(trade -> {
//                    trade.setInstrument(newTrade.getInstrument());
//                    trade.setCounterparty(newTrade.getCounterparty());
//                    trade.setBuySell(newTrade.getBuySell());
//                    trade.setNotional(newTrade.getNotional());
//                    trade.setCurrency(newTrade.getCurrency());
//                    return repository.save(trade);
//                })
//                .orElseGet(() -> {
//                    return repository.save(newTrade);
//                });
    }

    @DeleteMapping("/trades/{id}")
    void deleteTrade(@PathVariable String id) {
        tradeActionService.DeleteTrade(id);
//        repository.deleteById(id);
    }
}
