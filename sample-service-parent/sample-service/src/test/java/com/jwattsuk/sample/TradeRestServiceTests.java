package com.jwattsuk.sample;

import com.jwattsuk.sample.dao.TradeRepository;
import com.jwattsuk.sample.rest.TradeRestService;
import com.jwattsuk.sample.model.Trade;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TradeRestService.class)
public class TradeRestServiceTests {

    @MockBean
    private TradeRepository tradeRepository;

    @MockBean
    private SampleProcessor sampleProcessor;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldReturnTrade() throws Exception {

        long id = 1L;
        Trade trade = new Trade("SWAP","UBS",100L,"BUY","GBP");

        when(tradeRepository.findById(id)).thenReturn(Optional.of(trade));
        mockMvc.perform(get("/trades/{id}", id)).andExpect(status().isOk())
                .andExpect(jsonPath("$.instrument").value(trade.getInstrument()))
                .andExpect(jsonPath("$.buySell").value(trade.getBuySell()))
                .andExpect(jsonPath("$.currency").value(trade.getCurrency()))
                .andExpect(jsonPath("$.counterparty").value(trade.getCounterparty()))
                .andDo(print());
    }

    @Test
    void shouldReturnNotFoundTutorial() throws Exception {
        long id = 1L;

        when(tradeRepository.findById(id)).thenReturn(Optional.empty());
        mockMvc.perform(get("/trades/{id}", id))
                .andExpect(status().isNotFound())
                .andDo(print());
    }

}
