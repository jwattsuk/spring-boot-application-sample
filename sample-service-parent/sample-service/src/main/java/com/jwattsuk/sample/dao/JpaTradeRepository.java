package com.jwattsuk.sample.dao;

import com.jwattsuk.sample.model.Trade;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaTradeRepository extends JpaRepository<Trade, String> {
}
