package com.stockexchange.api.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.HashSet;
import org.junit.jupiter.api.Test;

public class StockTest {

  @Test
  public void testStock() {
    Stock stock = new Stock();
    stock.setId(1L);
    stock.setName("AKBNK");
    stock.setDescription("Akbank");
    stock.setCurrentPrice(new BigDecimal("7.45"));
    stock.setLastUpdate(new Timestamp(System.currentTimeMillis()));
    stock.setExchangesListedIn(new HashSet<>());

    assertEquals(1L, stock.getId());
    assertEquals("AKBNK", stock.getName());
    assertEquals("Akbank", stock.getDescription());
    assertEquals(new BigDecimal("7.45"), stock.getCurrentPrice());
  }
}
