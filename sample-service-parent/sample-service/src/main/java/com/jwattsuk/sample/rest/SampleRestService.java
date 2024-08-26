package com.jwattsuk.sample.rest;

import com.jwattsuk.sample.SampleProcessor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SampleRestService {

    private final SampleProcessor sampleProcessor;

    public SampleRestService(SampleProcessor sampleProcessor) {
        this.sampleProcessor = sampleProcessor;
    }

    @GetMapping(value = "/rest/deal")
    public String getDeal(Integer identifier) {
        return sampleProcessor.getDeal(identifier);
    }

}
