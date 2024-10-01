package com.jwattsuk.sample.rest;

import com.jwattsuk.sample.exception.TradeNotFoundException;
import com.jwattsuk.sample.model.Trade;
import com.jwattsuk.sample.services.TradeActionService;
import com.jwattsuk.sample.utils.TimingService;
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
        List<Trade> response;
        TimingService.init();
        var timer = TimingService.startTimer("GetAllTrades");
        try {
            response = tradeActionService.GetAllTrades();
            TimingService.endTimer(timer);
        } catch (Exception ex) {
            TimingService.endTimer(timer, ex);
            throw ex;
        } finally {
            TimingService.logTimings();
        }
        return response;
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
