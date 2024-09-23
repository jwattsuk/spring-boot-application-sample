package com.jwattsuk.sample.rest;

import com.jwattsuk.sample.exception.TradeNotFoundException;
import com.jwattsuk.sample.model.Trade;
import com.jwattsuk.sample.services.TradeActionService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class TradeRestService {

    private final TradeActionService tradeActionService;

    public TradeRestService(TradeActionService tradeActionService) {
        this.tradeActionService = tradeActionService;
    }

    @GetMapping("/trades")
    List<Trade> all() {
        return tradeActionService.GetAllTrades();
    }

    @PostMapping("/trades")
    Trade newTrade(@RequestBody Trade newTrade) {
        return tradeActionService.SaveNewTrade(newTrade);
    }

    @GetMapping(value = "/trades/{id}")
    public Trade one(@PathVariable String id) {
        return tradeActionService.GetTrade(id)
                .orElseThrow(() -> new TradeNotFoundException(id));
    }

    @PutMapping("/trades/{id}")
    Trade replaceTrade(@RequestBody Trade newTrade, @PathVariable String id) {
        return tradeActionService.UpdateTrade(newTrade, id);
    }

    @DeleteMapping("/trades/{id}")
    void deleteTrade(@PathVariable String id) {
        tradeActionService.DeleteTrade(id);
    }
}
