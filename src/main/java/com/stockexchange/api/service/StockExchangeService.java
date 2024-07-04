package com.stockexchange.api.service;

import com.stockexchange.api.exception.ResourceNotFoundException;
import com.stockexchange.api.model.Stock;
import com.stockexchange.api.model.StockExchange;
import com.stockexchange.api.repository.StockExchangeRepository;
import com.stockexchange.api.repository.StockRepository;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StockExchangeService {

  @Autowired private StockExchangeRepository stockExchangeRepository;

  @Autowired private StockRepository stockRepository;

  public StockExchange getStockExchangeByName(String name) {
    return stockExchangeRepository
        .findByName(name)
        .orElseThrow(
            () -> new ResourceNotFoundException("Stock exchange not found with name: " + name));
  }

  public StockExchange createStockExchange(StockExchange stockExchange) {
    if (stockExchangeRepository.existsByName(stockExchange.getName())) {
      throw new IllegalArgumentException("A stock exchange with the same name already exists.");
    }

    if (stockExchange.isLiveInMarket()
        && (stockExchange.getListedStocks() == null
            || stockExchange.getListedStocks().size() < 5)) {
      throw new IllegalArgumentException(
          "A stock exchange cannot be live in the market with fewer than 5 stocks.");
    }

    return stockExchangeRepository.save(stockExchange);
  }

  public Stock createStock(Stock stock) {
    if (stock.getName() == null
        || stock.getName().isEmpty()
        || stock.getDescription() == null
        || stock.getDescription().isEmpty()
        || stock.getCurrentPrice() == null
        || stock.getCurrentPrice().compareTo(BigDecimal.ZERO) <= 0) {
      throw new IllegalArgumentException("Stock properties cannot be empty or invalid.");
    }

    return stockRepository.save(stock);
  }

  public void addStockToExchangeById(String exchangeName, Long stockId) {
    StockExchange exchange =
        stockExchangeRepository
            .findByName(exchangeName)
            .orElseThrow(
                () ->
                    new ResourceNotFoundException(
                        "Stock exchange not found with name: " + exchangeName));
    Stock stock =
        stockRepository
            .findById(stockId)
            .orElseThrow(
                () -> new ResourceNotFoundException("Stock not found with id: " + stockId));
    exchange.getListedStocks().add(stock);
    if (exchange.getListedStocks().size() >= 5) {
      exchange.setLiveInMarket(true);
    }
    stockExchangeRepository.save(exchange);
  }

  public void addStockToExchangeByName(String exchangeName, String stockName) {
    StockExchange exchange =
        stockExchangeRepository
            .findByName(exchangeName)
            .orElseThrow(
                () ->
                    new ResourceNotFoundException(
                        "Stock exchange not found with name: " + exchangeName));
    Stock stock =
        stockRepository
            .findByName(stockName)
            .orElseThrow(
                () -> new ResourceNotFoundException("Stock not found with name: " + stockName));
    exchange.getListedStocks().add(stock);
    if (exchange.getListedStocks().size() >= 5) {
      exchange.setLiveInMarket(true);
    }
    stockExchangeRepository.save(exchange);
  }

  public void removeStockFromExchangeById(String exchangeName, Long stockId) {
    StockExchange exchange =
        stockExchangeRepository
            .findByName(exchangeName)
            .orElseThrow(
                () ->
                    new ResourceNotFoundException(
                        "Stock exchange not found with name: " + exchangeName));
    Stock stock =
        stockRepository
            .findById(stockId)
            .orElseThrow(
                () -> new ResourceNotFoundException("Stock not found with id: " + stockId));
    exchange.getListedStocks().remove(stock);
    if (exchange.getListedStocks().size() < 5) {
      exchange.setLiveInMarket(false);
    }
    stockExchangeRepository.save(exchange);
  }

  public void removeStockFromExchangeByName(String exchangeName, String stockName) {
    StockExchange exchange =
        stockExchangeRepository
            .findByName(exchangeName)
            .orElseThrow(
                () ->
                    new ResourceNotFoundException(
                        "Stock exchange not found with name: " + exchangeName));
    Stock stock =
        stockRepository
            .findByName(stockName)
            .orElseThrow(
                () -> new ResourceNotFoundException("Stock not found with name: " + stockName));
    exchange.getListedStocks().remove(stock);
    if (exchange.getListedStocks().size() < 5) {
      exchange.setLiveInMarket(false);
    }
    stockExchangeRepository.save(exchange);
  }

  public Stock updateStockPrice(Long stockId, BigDecimal newPrice) {
    Stock stock =
        stockRepository
            .findById(stockId)
            .orElseThrow(
                () -> new ResourceNotFoundException("Stock not found with id: " + stockId));
    stock.setCurrentPrice(newPrice);
    stock.setLastUpdate(new Timestamp(System.currentTimeMillis()));
    return stockRepository.save(stock);
  }

  public void deleteStock(Long stockId) {
    Stock stock =
        stockRepository
            .findById(stockId)
            .orElseThrow(
                () -> new ResourceNotFoundException("Stock not found with id: " + stockId));

    // Remove stock from all associated stock exchanges
    Set<StockExchange> stockExchanges = stock.getExchangesListedIn();
    for (StockExchange exchange : stockExchanges) {
      exchange.getListedStocks().remove(stock);
      stockExchangeRepository.save(exchange);
    }

    stockRepository.deleteById(stockId);
  }
}
