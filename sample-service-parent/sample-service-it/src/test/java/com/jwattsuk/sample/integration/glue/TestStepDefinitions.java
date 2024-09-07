package com.jwattsuk.sample.integration.glue;

import com.jwattsuk.sample.SampleServiceApplication;
import com.jwattsuk.sample.model.Trade;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.BeforeAll;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class TestStepDefinitions  {

    private static final Logger LOG = LoggerFactory.getLogger(TestStepDefinitions.class);

    private static final String BASE_URL = "http://localhost:8081/rest";

    private final List<Trade> trades = new ArrayList<>();

    @Given("^the following trades")
    public final void newTrades(final DataTable dataTable) {
        // Copy from Datatable to object
        List<Map<String,String>> dataTableMaps = dataTable.asMaps();
        for(Map<String,String> map : dataTableMaps) {
            Trade trade = new Trade(map.get("INSTRUMENT"), map.get("COUNTERPARTY"),
                    Long.valueOf(map.get("NOTIONAL")), map.get("BUYSELL"), map.get("CURRENCY"));
            trades.add(trade);
        }
    }

    @When("the trades are persisted")
    public final void saveTrades() {
        // Iterate the trades and post each object to the service
        for(Trade trade : trades) {
            RestAssured.baseURI = BASE_URL;
            RequestSpecification request = RestAssured.given();
            request.header("Content-Type", "application/json");
            request.body(trade);
            Response response = request.post("/trades");
            LOG.info(response.asString());
            Trade newTrade = response.getBody().as(Trade.class);
            trades.set(trades.indexOf(trade), newTrade);
        }
    }

    @Then("all trades can be retrieved")
    public final void getTrades() throws IOException {
        // Run a get for each trade in the list we've just persisted
        RestAssured.baseURI = BASE_URL;
        RequestSpecification request = RestAssured.given();
        request.header("Content-Type", "application/json");

        for(Trade trade : trades) {
            Response response = request.get(String.format("/trades/%s", trade.getId()));
            assertThat(response.getStatusCode(), is(200));
        }
    }
}
