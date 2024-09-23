package com.jwattsuk.sample;

import com.jwattsuk.sample.model.Trade;
import com.jwattsuk.sample.rest.TradeRestService;
import com.jwattsuk.sample.services.TradeActionService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TradeRestService.class)
public class TradeRestServiceTests {

    private final String ID = "66e6d5bc57beac65f5214b1c";
    @MockBean
    private TradeActionService tradeActionService;
    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldReturnTrade() throws Exception {

        Trade trade = new Trade("SWAP", "UBS", 100L, "BUY", "GBP");

        when(tradeActionService.GetTrade(ID)).thenReturn(Optional.of(trade));
        mockMvc.perform(get("/trades/{id}", ID)).andExpect(status().isOk())
                .andExpect(jsonPath("$.instrument").value(trade.getInstrument()))
                .andExpect(jsonPath("$.buySell").value(trade.getBuySell()))
                .andExpect(jsonPath("$.currency").value(trade.getCurrency()))
                .andExpect(jsonPath("$.counterparty").value(trade.getCounterparty()))
                .andDo(print());
    }

    @Test
    void shouldReturnNotFoundTutorial() throws Exception {

        when(tradeActionService.GetTrade(ID)).thenReturn(Optional.empty());
        mockMvc.perform(get("/trades/{id}", ID))
                .andExpect(status().isNotFound())
                .andDo(print());
    }

}
