package com.stockexchange.api.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.stockexchange.api.exception.GlobalExceptionHandler;
import com.stockexchange.api.exception.ResourceNotFoundException;
import com.stockexchange.api.model.Stock;
import com.stockexchange.api.model.StockExchange;
import com.stockexchange.api.service.StockExchangeService;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

public class StockExchangeControllerTest {

  @Autowired private MockMvc mockMvc;

  @Mock private StockExchangeService stockExchangeService;

  @InjectMocks private StockExchangeController stockExchangeController;

  @BeforeEach
  public void setUp() {
    MockitoAnnotations.openMocks(this);
    this.mockMvc =
        MockMvcBuilders.standaloneSetup(stockExchangeController)
            .setControllerAdvice(new GlobalExceptionHandler())
            .build();
  }

  @Test
  public void testCreateStockExchange() throws Exception {
    StockExchange exchange = new StockExchange();
    exchange.setName("BIST");

    when(stockExchangeService.createStockExchange(any())).thenReturn(exchange);

    mockMvc
        .perform(
            post("/api/v1/stock-exchange")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(exchange)))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.name").value("BIST"));

    verify(stockExchangeService, times(1)).createStockExchange(any());
  }

  @Test
  public void testCreateStockExchangeAlreadyExists() throws Exception {
    StockExchange exchange = new StockExchange();
    exchange.setName("BIST");

    when(stockExchangeService.createStockExchange(any()))
        .thenThrow(
            new IllegalArgumentException("A stock exchange with the same name already exists."));

    mockMvc
        .perform(
            post("/api/v1/stock-exchange")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(exchange)))
        .andExpect(status().isBadRequest())
        .andExpect(content().string("A stock exchange with the same name already exists."));

    verify(stockExchangeService, times(1)).createStockExchange(any());
  }

  @Test
  public void testCreateStock() throws Exception {
    Stock stock = new Stock();
    stock.setName("AKBNK");

    when(stockExchangeService.createStock(any())).thenReturn(stock);

    mockMvc
        .perform(
            post("/api/v1/stock")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(stock)))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.name").value("AKBNK"));

    verify(stockExchangeService, times(1)).createStock(any());
  }

  @Test
  public void testGetStockExchange() throws Exception {
    StockExchange exchange = new StockExchange();
    exchange.setName("BIST");

    when(stockExchangeService.getStockExchangeByName("BIST")).thenReturn(exchange);

    mockMvc
        .perform(get("/api/v1/stock-exchange/BIST").contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.name").value("BIST"));

    verify(stockExchangeService, times(1)).getStockExchangeByName("BIST");
  }

  @Test
  public void testGetStockExchangeNotFound() throws Exception {
    when(stockExchangeService.getStockExchangeByName("BIST"))
        .thenThrow(new ResourceNotFoundException("Stock exchange not found"));

    mockMvc
        .perform(get("/api/v1/stock-exchange/BIST").contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isNotFound());

    verify(stockExchangeService, times(1)).getStockExchangeByName("BIST");
  }

  @Test
  public void testAddStockToExchangeById() throws Exception {
    doNothing().when(stockExchangeService).addStockToExchangeById("BIST", 1L);

    mockMvc
        .perform(
            post("/api/v1/stock-exchange/BIST/add-stock-by-id/1")
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isNoContent());

    verify(stockExchangeService, times(1)).addStockToExchangeById("BIST", 1L);
  }

  @Test
  public void testAddStockToExchangeByIdStockNotFound() throws Exception {
    doThrow(new ResourceNotFoundException("Stock not found"))
        .when(stockExchangeService)
        .addStockToExchangeById("BIST", 1L);

    mockMvc
        .perform(
            post("/api/v1/stock-exchange/BIST/add-stock-by-id/1")
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isNotFound())
        .andExpect(content().string("Stock not found"));

    verify(stockExchangeService, times(1)).addStockToExchangeById("BIST", 1L);
  }

  @Test
  public void testAddStockToExchangeByIdStockExchangeNotFound() throws Exception {
    doThrow(new ResourceNotFoundException("Stock exchange not found"))
        .when(stockExchangeService)
        .addStockToExchangeById("BIST", 1L);

    mockMvc
        .perform(
            post("/api/v1/stock-exchange/BIST/add-stock-by-id/1")
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isNotFound())
        .andExpect(content().string("Stock exchange not found"));

    verify(stockExchangeService, times(1)).addStockToExchangeById("BIST", 1L);
  }

  @Test
  public void testAddStockToExchangeByName() throws Exception {
    doNothing().when(stockExchangeService).addStockToExchangeByName("BIST", "AKBNK");

    mockMvc
        .perform(
            post("/api/v1/stock-exchange/BIST/add-stock-by-name/AKBNK")
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isNoContent());

    verify(stockExchangeService, times(1)).addStockToExchangeByName("BIST", "AKBNK");
  }

  @Test
  public void testAddStockToExchangeByNameStockNotFound() throws Exception {
    doThrow(new ResourceNotFoundException("Stock not found"))
        .when(stockExchangeService)
        .addStockToExchangeByName("BIST", "AKBNK");

    mockMvc
        .perform(
            post("/api/v1/stock-exchange/BIST/add-stock-by-name/AKBNK")
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isNotFound())
        .andExpect(content().string("Stock not found"));

    verify(stockExchangeService, times(1)).addStockToExchangeByName("BIST", "AKBNK");
  }

  @Test
  public void testAddStockToExchangeByNameStockExchangeNotFound() throws Exception {
    doThrow(new ResourceNotFoundException("Stock exchange not found"))
        .when(stockExchangeService)
        .addStockToExchangeByName("BIST", "AKBNK");

    mockMvc
        .perform(
            post("/api/v1/stock-exchange/BIST/add-stock-by-name/AKBNK")
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isNotFound())
        .andExpect(content().string("Stock exchange not found"));

    verify(stockExchangeService, times(1)).addStockToExchangeByName("BIST", "AKBNK");
  }

  @Test
  public void testRemoveStockFromExchangeById() throws Exception {
    doNothing().when(stockExchangeService).removeStockFromExchangeById("BIST", 1L);

    mockMvc
        .perform(
            post("/api/v1/stock-exchange/BIST/remove-stock-by-id/1")
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isNoContent());

    verify(stockExchangeService, times(1)).removeStockFromExchangeById("BIST", 1L);
  }

  @Test
  public void testRemoveStockFromExchangeByIdStockNotFound() throws Exception {
    doThrow(new ResourceNotFoundException("Stock not found"))
        .when(stockExchangeService)
        .removeStockFromExchangeById("BIST", 1L);

    mockMvc
        .perform(
            post("/api/v1/stock-exchange/BIST/remove-stock-by-id/1")
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isNotFound())
        .andExpect(content().string("Stock not found"));

    verify(stockExchangeService, times(1)).removeStockFromExchangeById("BIST", 1L);
  }

  @Test
  public void testRemoveStockFromExchangeByIdStockExchangeNotFound() throws Exception {
    doThrow(new ResourceNotFoundException("Stock exchange not found"))
        .when(stockExchangeService)
        .removeStockFromExchangeById("BIST", 1L);

    mockMvc
        .perform(
            post("/api/v1/stock-exchange/BIST/remove-stock-by-id/1")
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isNotFound())
        .andExpect(content().string("Stock exchange not found"));

    verify(stockExchangeService, times(1)).removeStockFromExchangeById("BIST", 1L);
  }

  @Test
  public void testRemoveStockFromExchangeByName() throws Exception {
    doNothing().when(stockExchangeService).removeStockFromExchangeByName("BIST", "AKBNK");

    mockMvc
        .perform(
            post("/api/v1/stock-exchange/BIST/remove-stock-by-name/AKBNK")
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isNoContent());

    verify(stockExchangeService, times(1)).removeStockFromExchangeByName("BIST", "AKBNK");
  }

  @Test
  public void testRemoveStockFromExchangeByNameStockNotFound() throws Exception {
    doThrow(new ResourceNotFoundException("Stock not found"))
        .when(stockExchangeService)
        .removeStockFromExchangeByName("BIST", "AKBNK");

    mockMvc
        .perform(
            post("/api/v1/stock-exchange/BIST/remove-stock-by-name/AKBNK")
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isNotFound())
        .andExpect(content().string("Stock not found"));

    verify(stockExchangeService, times(1)).removeStockFromExchangeByName("BIST", "AKBNK");
  }

  @Test
  public void testRemoveStockFromExchangeByNameStockExchangeNotFound() throws Exception {
    doThrow(new ResourceNotFoundException("Stock exchange not found"))
        .when(stockExchangeService)
        .removeStockFromExchangeByName("BIST", "AKBNK");

    mockMvc
        .perform(
            post("/api/v1/stock-exchange/BIST/remove-stock-by-name/AKBNK")
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isNotFound())
        .andExpect(content().string("Stock exchange not found"));

    verify(stockExchangeService, times(1)).removeStockFromExchangeByName("BIST", "AKBNK");
  }

  @Test
  public void testUpdateStockPrice() throws Exception {
    Stock stock = new Stock();
    stock.setId(1L);
    stock.setCurrentPrice(new BigDecimal("8.00"));

    when(stockExchangeService.updateStockPrice(eq(1L), any(BigDecimal.class))).thenReturn(stock);

    Map<String, BigDecimal> request = new HashMap<>();
    request.put("newPrice", new BigDecimal("8.00"));

    mockMvc
        .perform(
            put("/api/v1/stock/1/update-price")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(request)))
        .andExpect(status().isOk());

    verify(stockExchangeService, times(1)).updateStockPrice(eq(1L), any(BigDecimal.class));
  }

  @Test
  public void testUpdateStockPriceNotFound() throws Exception {
    when(stockExchangeService.updateStockPrice(eq(1L), any(BigDecimal.class)))
        .thenThrow(new ResourceNotFoundException("Stock not found"));

    Map<String, BigDecimal> request = new HashMap<>();
    request.put("newPrice", new BigDecimal("8.00"));

    mockMvc
        .perform(
            put("/api/v1/stock/1/update-price")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(request)))
        .andExpect(status().isNotFound());

    verify(stockExchangeService, times(1)).updateStockPrice(eq(1L), any(BigDecimal.class));
  }

  @Test
  public void testDeleteStock() throws Exception {
    doNothing().when(stockExchangeService).deleteStock(1L);

    mockMvc
        .perform(delete("/api/v1/stock/1").contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isNoContent());

    verify(stockExchangeService, times(1)).deleteStock(1L);
  }

  @Test
  public void testDeleteStockNotFound() throws Exception {
    doThrow(new ResourceNotFoundException("Stock not found"))
        .when(stockExchangeService)
        .deleteStock(1L);

    mockMvc
        .perform(delete("/api/v1/stock/1").contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isNotFound())
        .andExpect(
            result ->
                assertTrue(result.getResolvedException() instanceof ResourceNotFoundException))
        .andExpect(
            result -> assertEquals("Stock not found", result.getResolvedException().getMessage()));

    verify(stockExchangeService, times(1)).deleteStock(1L);
  }

  @Test
  public void testCreateStockWithInvalidProperties() throws Exception {
    Stock stock = new Stock();
    stock.setName("");
    stock.setDescription("");
    stock.setCurrentPrice(BigDecimal.ZERO);

    when(stockExchangeService.createStock(any()))
        .thenThrow(new IllegalArgumentException("Stock properties cannot be empty or invalid."));

    mockMvc
        .perform(
            post("/api/v1/stock")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(stock)))
        .andExpect(status().isBadRequest())
        .andExpect(content().string("Stock properties cannot be empty or invalid."));

    verify(stockExchangeService, times(1)).createStock(any());
  }

  @Test
  public void testAddStockToExchangeByIdWithInvalidStock() throws Exception {
    doThrow(new IllegalArgumentException("Invalid stock ID"))
        .when(stockExchangeService)
        .addStockToExchangeById("BIST", 1L);

    mockMvc
        .perform(
            post("/api/v1/stock-exchange/BIST/add-stock-by-id/1")
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isBadRequest())
        .andExpect(content().string("Invalid stock ID"));

    verify(stockExchangeService, times(1)).addStockToExchangeById("BIST", 1L);
  }

  @Test
  public void testAddStockToExchangeByNameWithInvalidStock() throws Exception {
    doThrow(new IllegalArgumentException("Invalid stock name"))
        .when(stockExchangeService)
        .addStockToExchangeByName("BIST", "INVALID");

    mockMvc
        .perform(
            post("/api/v1/stock-exchange/BIST/add-stock-by-name/INVALID")
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isBadRequest())
        .andExpect(content().string("Invalid stock name"));

    verify(stockExchangeService, times(1)).addStockToExchangeByName("BIST", "INVALID");
  }

  @Test
  public void testRemoveStockFromExchangeByIdWithInvalidStock() throws Exception {
    doThrow(new IllegalArgumentException("Invalid stock ID"))
        .when(stockExchangeService)
        .removeStockFromExchangeById("BIST", 1L);

    mockMvc
        .perform(
            post("/api/v1/stock-exchange/BIST/remove-stock-by-id/1")
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isBadRequest())
        .andExpect(content().string("Invalid stock ID"));

    verify(stockExchangeService, times(1)).removeStockFromExchangeById("BIST", 1L);
  }

  @Test
  public void testRemoveStockFromExchangeByNameWithInvalidStock() throws Exception {
    doThrow(new IllegalArgumentException("Invalid stock name"))
        .when(stockExchangeService)
        .removeStockFromExchangeByName("BIST", "INVALID");

    mockMvc
        .perform(
            post("/api/v1/stock-exchange/BIST/remove-stock-by-name/INVALID")
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isBadRequest())
        .andExpect(content().string("Invalid stock name"));

    verify(stockExchangeService, times(1)).removeStockFromExchangeByName("BIST", "INVALID");
  }

  @Test
  public void testCreateStockExchangeWithFewerThan5Stocks() throws Exception {
    StockExchange exchange = new StockExchange();
    exchange.setName("BIST");
    exchange.setLiveInMarket(true);
    exchange.setListedStocks(new HashSet<>());

    when(stockExchangeService.createStockExchange(any()))
        .thenThrow(
            new IllegalArgumentException(
                "A stock exchange cannot be live in the market with fewer than 5 stocks."));

    mockMvc
        .perform(
            post("/api/v1/stock-exchange")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(exchange)))
        .andExpect(status().isBadRequest())
        .andExpect(
            content()
                .string("A stock exchange cannot be live in the market with fewer than 5 stocks."));

    verify(stockExchangeService, times(1)).createStockExchange(any());
  }

  @Test
  public void testRemoveStockFromExchangeByIdUpdatesLiveInMarket() throws Exception {
    StockExchange exchange = new StockExchange();
    exchange.setName("BIST");
    exchange.setListedStocks(new HashSet<>(Collections.nCopies(5, new Stock())));
    exchange.setLiveInMarket(true);
    Stock stock = new Stock();
    stock.setId(1L);

    doAnswer(
            invocation -> {
              exchange.getListedStocks().remove(stock);
              if (exchange.getListedStocks().size() < 5) {
                exchange.setLiveInMarket(false);
              }
              return null;
            })
        .when(stockExchangeService)
        .removeStockFromExchangeById("BIST", 1L);

    mockMvc
        .perform(
            post("/api/v1/stock-exchange/BIST/remove-stock-by-id/1")
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isNoContent());

    assertFalse(exchange.isLiveInMarket());
  }

  @Test
  public void testRemoveStockFromExchangeByNameUpdatesLiveInMarket() throws Exception {
    StockExchange exchange = new StockExchange();
    exchange.setName("BIST");
    exchange.setListedStocks(new HashSet<>(Collections.nCopies(5, new Stock())));
    exchange.setLiveInMarket(true);
    Stock stock = new Stock();
    stock.setName("AKBNK");

    doAnswer(
            invocation -> {
              exchange.getListedStocks().remove(stock);
              if (exchange.getListedStocks().size() < 5) {
                exchange.setLiveInMarket(false);
              }
              return null;
            })
        .when(stockExchangeService)
        .removeStockFromExchangeByName("BIST", "AKBNK");

    mockMvc
        .perform(
            post("/api/v1/stock-exchange/BIST/remove-stock-by-name/AKBNK")
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isNoContent());

    assertFalse(exchange.isLiveInMarket());
  }
}
