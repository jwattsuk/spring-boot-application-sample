package com.jwattsuk.sample;

public class SampleProcessor {
    public String getDeal(Long identifier) {
        return String.format("HelloWorld %d", identifier+1);
    }
}
