package com.jwattsuk.sample.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;

import java.util.Objects;

@Entity
public class Trade {

    private @Id
    @UuidGenerator String id;
    @Setter
    private String instrument;
    private String counterparty;
    private Long notional;
    private String buySell;
    private String currency;

    public Trade() {
    }

    public Trade(String instrument, String counterparty, Long notional, String buySell, String currency) {
        this.instrument = instrument;
        this.counterparty = counterparty;
        this.notional = notional;
        this.buySell = buySell;
        this.currency = currency;
    }

    public String getId() {
        return id;
    }

    public String getInstrument() {
        return instrument;
    }

    public String getCounterparty() {
        return counterparty;
    }

    public void setCounterparty(String counterparty) {
        this.counterparty = counterparty;
    }

    public Long getNotional() {
        return notional;
    }

    public void setNotional(Long notional) {
        this.notional = notional;
    }

    public String getBuySell() {
        return buySell;
    }

    public void setBuySell(String buySell) {
        this.buySell = buySell;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Trade trade)) return false;
        return Objects.equals(id, trade.id) && Objects.equals(instrument, trade.instrument) && Objects.equals(counterparty, trade.counterparty) && Objects.equals(notional, trade.notional) && Objects.equals(buySell, trade.buySell) && Objects.equals(currency, trade.currency);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, instrument, counterparty, notional, buySell, currency);
    }

    @Override
    public String toString() {
        return "Trade{" +
                "id=" + id +
                ", instrument='" + instrument + '\'' +
                ", counterparty='" + counterparty + '\'' +
                ", notional=" + notional +
                ", buySell='" + buySell + '\'' +
                ", currency='" + currency + '\'' +
                '}';
    }
}
