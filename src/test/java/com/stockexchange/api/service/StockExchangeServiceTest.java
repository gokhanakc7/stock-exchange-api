package com.stockexchange.api.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.stockexchange.api.exception.ResourceNotFoundException;
import com.stockexchange.api.model.Stock;
import com.stockexchange.api.model.StockExchange;
import com.stockexchange.api.repository.StockExchangeRepository;
import com.stockexchange.api.repository.StockRepository;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class StockExchangeServiceTest {

  @Mock private StockExchangeRepository stockExchangeRepository;

  @Mock private StockRepository stockRepository;

  @InjectMocks private StockExchangeService stockExchangeService;

  @BeforeEach
  public void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  public void testGetStockExchangeByName() {
    StockExchange exchange = new StockExchange();
    exchange.setName("BIST");
    when(stockExchangeRepository.findByName("BIST")).thenReturn(Optional.of(exchange));

    StockExchange result = stockExchangeService.getStockExchangeByName("BIST");
    assertEquals("BIST", result.getName());
  }

  @Test
  public void testGetStockExchangeByNameNotFound() {
    when(stockExchangeRepository.findByName("BIST")).thenReturn(Optional.empty());
    assertThrows(
        ResourceNotFoundException.class, () -> stockExchangeService.getStockExchangeByName("BIST"));
  }

  @Test
  public void testCreateStockExchange() {
    StockExchange exchange = new StockExchange();
    exchange.setName("BIST");
    exchange.setListedStocks(new HashSet<>());
    when(stockExchangeRepository.existsByName("BIST")).thenReturn(false);
    when(stockExchangeRepository.save(exchange)).thenReturn(exchange);

    StockExchange result = stockExchangeService.createStockExchange(exchange);
    assertEquals("BIST", result.getName());
  }

  @Test
  public void testCreateStockExchangeAlreadyExists() {
    StockExchange exchange = new StockExchange();
    exchange.setName("BIST");
    when(stockExchangeRepository.existsByName("BIST")).thenReturn(true);

    assertThrows(
        IllegalArgumentException.class, () -> stockExchangeService.createStockExchange(exchange));
  }

  @Test
  public void testCreateStockExchangeInsufficientStocks() {
    StockExchange exchange = new StockExchange();
    exchange.setName("BIST");
    exchange.setLiveInMarket(true);
    exchange.setListedStocks(new HashSet<>());

    when(stockExchangeRepository.existsByName("BIST")).thenReturn(false);

    assertThrows(
        IllegalArgumentException.class, () -> stockExchangeService.createStockExchange(exchange));
  }

  @Test
  public void testAddStockToExchangeById() {
    StockExchange exchange = new StockExchange();
    exchange.setName("BIST");
    exchange.setListedStocks(new HashSet<>());
    Stock stock = new Stock();
    stock.setId(1L);
    when(stockExchangeRepository.findByName("BIST")).thenReturn(Optional.of(exchange));
    when(stockRepository.findById(1L)).thenReturn(Optional.of(stock));
    when(stockExchangeRepository.save(exchange)).thenReturn(exchange);

    stockExchangeService.addStockToExchangeById("BIST", 1L);
    assertEquals(1, exchange.getListedStocks().size());
  }

  @Test
  public void testAddStockToExchangeByIdStockNotFound() {
    StockExchange exchange = new StockExchange();
    exchange.setName("BIST");
    when(stockExchangeRepository.findByName("BIST")).thenReturn(Optional.of(exchange));
    when(stockRepository.findById(1L)).thenReturn(Optional.empty());

    assertThrows(
        ResourceNotFoundException.class,
        () -> stockExchangeService.addStockToExchangeById("BIST", 1L));
  }

  @Test
  public void testAddStockToExchangeByName() {
    StockExchange exchange = new StockExchange();
    exchange.setName("BIST");
    exchange.setListedStocks(new HashSet<>());
    Stock stock = new Stock();
    stock.setName("AKBNK");
    when(stockExchangeRepository.findByName("BIST")).thenReturn(Optional.of(exchange));
    when(stockRepository.findByName("AKBNK")).thenReturn(Optional.of(stock));
    when(stockExchangeRepository.save(exchange)).thenReturn(exchange);

    stockExchangeService.addStockToExchangeByName("BIST", "AKBNK");
    assertEquals(1, exchange.getListedStocks().size());
  }

  @Test
  public void testAddStockToExchangeByNameStockNotFound() {
    StockExchange exchange = new StockExchange();
    exchange.setName("BIST");
    when(stockExchangeRepository.findByName("BIST")).thenReturn(Optional.of(exchange));
    when(stockRepository.findByName("AKBNK")).thenReturn(Optional.empty());

    assertThrows(
        ResourceNotFoundException.class,
        () -> stockExchangeService.addStockToExchangeByName("BIST", "AKBNK"));
  }

  @Test
  public void testRemoveStockFromExchangeById() {
    StockExchange exchange = new StockExchange();
    exchange.setName("BIST");
    exchange.setListedStocks(new HashSet<>());
    Stock stock = new Stock();
    stock.setId(1L);
    exchange.getListedStocks().add(stock);
    when(stockExchangeRepository.findByName("BIST")).thenReturn(Optional.of(exchange));
    when(stockRepository.findById(1L)).thenReturn(Optional.of(stock));
    when(stockExchangeRepository.save(exchange)).thenReturn(exchange);

    stockExchangeService.removeStockFromExchangeById("BIST", 1L);
    assertEquals(0, exchange.getListedStocks().size());
  }

  @Test
  public void testRemoveStockFromExchangeByIdStockNotFound() {
    StockExchange exchange = new StockExchange();
    exchange.setName("BIST");
    when(stockExchangeRepository.findByName("BIST")).thenReturn(Optional.of(exchange));
    when(stockRepository.findById(1L)).thenReturn(Optional.empty());

    assertThrows(
        ResourceNotFoundException.class,
        () -> stockExchangeService.removeStockFromExchangeById("BIST", 1L));
  }

  @Test
  public void testRemoveStockFromExchangeByName() {
    StockExchange exchange = new StockExchange();
    exchange.setName("BIST");
    exchange.setListedStocks(new HashSet<>());
    Stock stock = new Stock();
    stock.setName("AKBNK");
    exchange.getListedStocks().add(stock);
    when(stockExchangeRepository.findByName("BIST")).thenReturn(Optional.of(exchange));
    when(stockRepository.findByName("AKBNK")).thenReturn(Optional.of(stock));
    when(stockExchangeRepository.save(exchange)).thenReturn(exchange);

    stockExchangeService.removeStockFromExchangeByName("BIST", "AKBNK");
    assertEquals(0, exchange.getListedStocks().size());
  }

  @Test
  public void testRemoveStockFromExchangeByNameStockNotFound() {
    StockExchange exchange = new StockExchange();
    exchange.setName("BIST");
    when(stockExchangeRepository.findByName("BIST")).thenReturn(Optional.of(exchange));
    when(stockRepository.findByName("AKBNK")).thenReturn(Optional.empty());

    assertThrows(
        ResourceNotFoundException.class,
        () -> stockExchangeService.removeStockFromExchangeByName("BIST", "AKBNK"));
  }

  @Test
  public void testUpdateStockPrice() {
    Stock stock = new Stock();
    stock.setId(1L);
    stock.setCurrentPrice(new BigDecimal("7.45"));
    stock.setLastUpdate(new Timestamp(System.currentTimeMillis()));
    when(stockRepository.findById(1L)).thenReturn(Optional.of(stock));
    when(stockRepository.save(stock)).thenReturn(stock);

    Stock updatedStock = stockExchangeService.updateStockPrice(1L, new BigDecimal("8.00"));
    assertEquals(new BigDecimal("8.00"), updatedStock.getCurrentPrice());
  }

  @Test
  public void testUpdateStockPriceNotFound() {
    when(stockRepository.findById(1L)).thenReturn(Optional.empty());
    assertThrows(
        ResourceNotFoundException.class,
        () -> stockExchangeService.updateStockPrice(1L, new BigDecimal("8.00")));
  }

  @Test
  public void testDeleteStock() {
    Stock stock = new Stock();
    stock.setId(1L);
    StockExchange exchange = new StockExchange();
    exchange.setName("BIST");
    exchange.setListedStocks(new HashSet<>());
    stock.setExchangesListedIn(new HashSet<>());
    stock.getExchangesListedIn().add(exchange);
    when(stockRepository.findById(1L)).thenReturn(Optional.of(stock));

    stockExchangeService.deleteStock(1L);
    verify(stockRepository, times(1)).deleteById(1L);
  }

  @Test
  public void testDeleteStockNotFound() {
    when(stockRepository.findById(1L)).thenReturn(Optional.empty());
    assertThrows(ResourceNotFoundException.class, () -> stockExchangeService.deleteStock(1L));
  }

  @Test
  public void testCreateStock() {
    Stock stock = new Stock();
    stock.setName("AKBNK");
    stock.setDescription("Akbank");
    stock.setCurrentPrice(new BigDecimal("7.45"));

    when(stockRepository.save(stock)).thenReturn(stock);

    Stock result = stockExchangeService.createStock(stock);
    assertEquals("AKBNK", result.getName());
    assertEquals("Akbank", result.getDescription());
    assertEquals(new BigDecimal("7.45"), result.getCurrentPrice());
  }

  @Test
  public void testCreateStockWithEmptyProperties() {
    Stock stock = new Stock();
    stock.setName("");
    stock.setDescription("");
    stock.setCurrentPrice(BigDecimal.ZERO);

    assertThrows(IllegalArgumentException.class, () -> stockExchangeService.createStock(stock));
  }

  @Test
  public void testCreateStockWithNullName() {
    Stock stock = new Stock();
    stock.setName(null);
    stock.setDescription("Akbank");
    stock.setCurrentPrice(new BigDecimal("7.45"));

    assertThrows(IllegalArgumentException.class, () -> stockExchangeService.createStock(stock));
  }

  @Test
  public void testCreateStockWithEmptyName() {
    Stock stock = new Stock();
    stock.setName("");
    stock.setDescription("Akbank");
    stock.setCurrentPrice(new BigDecimal("7.45"));

    assertThrows(IllegalArgumentException.class, () -> stockExchangeService.createStock(stock));
  }

  @Test
  public void testCreateStockWithNullDescription() {
    Stock stock = new Stock();
    stock.setName("AKBNK");
    stock.setDescription(null);
    stock.setCurrentPrice(new BigDecimal("7.45"));

    assertThrows(IllegalArgumentException.class, () -> stockExchangeService.createStock(stock));
  }

  @Test
  public void testCreateStockWithEmptyDescription() {
    Stock stock = new Stock();
    stock.setName("AKBNK");
    stock.setDescription("");
    stock.setCurrentPrice(new BigDecimal("7.45"));

    assertThrows(IllegalArgumentException.class, () -> stockExchangeService.createStock(stock));
  }

  @Test
  public void testCreateStockWithNullPrice() {
    Stock stock = new Stock();
    stock.setName("AKBNK");
    stock.setDescription("Akbank");
    stock.setCurrentPrice(null);

    assertThrows(IllegalArgumentException.class, () -> stockExchangeService.createStock(stock));
  }

  @Test
  public void testCreateStockWithZeroPrice() {
    Stock stock = new Stock();
    stock.setName("AKBNK");
    stock.setDescription("Akbank");
    stock.setCurrentPrice(BigDecimal.ZERO);

    assertThrows(IllegalArgumentException.class, () -> stockExchangeService.createStock(stock));
  }
}
