package com.stockexchange.api.repository;

import static org.junit.jupiter.api.Assertions.*;

import com.stockexchange.api.model.StockExchange;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.transaction.annotation.Transactional;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class StockExchangeRepositoryTest {

  @Autowired private StockExchangeRepository stockExchangeRepository;

  private StockExchange stockExchange;

  @BeforeEach
  public void setUp() {
    stockExchange = new StockExchange();
    stockExchange.setName("BIST");
    stockExchange.setDescription("Borsa Istanbul");
    stockExchange.setLiveInMarket(true);
    stockExchangeRepository.save(stockExchange);
  }

  @Test
  @Transactional
  public void testFindByName() {
    Optional<StockExchange> foundStockExchange = stockExchangeRepository.findByName("BIST");
    assertTrue(foundStockExchange.isPresent());
    assertEquals("BIST", foundStockExchange.get().getName());
  }

  @Test
  @Transactional
  public void testFindByNameNotFound() {
    Optional<StockExchange> foundStockExchange = stockExchangeRepository.findByName("JPNN");
    assertFalse(foundStockExchange.isPresent());
  }

  @Test
  @Transactional
  public void testSaveStockExchange() {
    StockExchange newStockExchange = new StockExchange();
    newStockExchange.setName("NASDAQ");
    newStockExchange.setDescription(
        "National Association of Securities Dealers Automated Quotations");
    newStockExchange.setLiveInMarket(true);
    StockExchange savedStockExchange = stockExchangeRepository.save(newStockExchange);

    assertNotNull(savedStockExchange);
    assertEquals("NASDAQ", savedStockExchange.getName());
  }

  @Test
  @Transactional
  public void testDeleteStockExchange() {
    stockExchangeRepository.delete(stockExchange);
    Optional<StockExchange> foundStockExchange =
        stockExchangeRepository.findById(stockExchange.getId());
    assertFalse(foundStockExchange.isPresent());
  }
}
