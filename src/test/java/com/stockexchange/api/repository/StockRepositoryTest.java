package com.stockexchange.api.repository;

import static org.junit.jupiter.api.Assertions.*;

import com.stockexchange.api.model.Stock;
import java.math.BigDecimal;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.transaction.annotation.Transactional;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class StockRepositoryTest {

  @Autowired private StockRepository stockRepository;

  private Stock stock;

  @BeforeEach
  public void setUp() {
    stock = new Stock();
    stock.setName("AKBNK");
    stock.setDescription("Akbank");
    stock.setCurrentPrice(new BigDecimal("7.45"));
    stockRepository.save(stock);
  }

  @Test
  @Transactional
  public void testFindByName() {
    Optional<Stock> foundStock = stockRepository.findByName("AKBNK");
    assertTrue(foundStock.isPresent());
    assertEquals("AKBNK", foundStock.get().getName());
  }

  @Test
  @Transactional
  public void testFindByNameNotFound() {
    Optional<Stock> foundStock = stockRepository.findByName("VAKBN");
    assertFalse(foundStock.isPresent());
  }

  @Test
  @Transactional
  public void testSaveStock() {
    Stock newStock = new Stock();
    newStock.setName("GARAN");
    newStock.setDescription("Garanti Bankası");
    newStock.setCurrentPrice(new BigDecimal("8.75"));
    Stock savedStock = stockRepository.save(newStock);

    assertNotNull(savedStock);
    assertEquals("GARAN", savedStock.getName());
  }

  @Test
  @Transactional
  public void testDeleteStock() {
    stockRepository.delete(stock);
    Optional<Stock> foundStock = stockRepository.findById(stock.getId());
    assertFalse(foundStock.isPresent());
  }
}
