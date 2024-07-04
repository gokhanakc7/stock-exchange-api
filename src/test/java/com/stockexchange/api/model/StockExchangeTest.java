package com.stockexchange.api.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.HashSet;
import org.junit.jupiter.api.Test;

public class StockExchangeTest {

  @Test
  public void testStockExchange() {
    StockExchange exchange = new StockExchange();
    exchange.setId(1L);
    exchange.setName("BIST");
    exchange.setDescription("Borsa Istanbul");
    exchange.setLiveInMarket(true);
    exchange.setListedStocks(new HashSet<>());

    assertEquals(1L, exchange.getId());
    assertEquals("BIST", exchange.getName());
    assertEquals("Borsa Istanbul", exchange.getDescription());
    assertEquals(true, exchange.isLiveInMarket());
  }
}
