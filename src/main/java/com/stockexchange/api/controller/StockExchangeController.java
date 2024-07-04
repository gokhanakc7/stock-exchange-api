package com.stockexchange.api.controller;

import com.stockexchange.api.exception.ResourceNotFoundException;
import com.stockexchange.api.model.Stock;
import com.stockexchange.api.model.StockExchange;
import com.stockexchange.api.service.StockExchangeService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import java.math.BigDecimal;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
public class StockExchangeController {

  @Autowired private StockExchangeService stockExchangeService;

  @ApiOperation(value = "Create a new stock exchange", response = StockExchange.class)
  @ApiResponses(
      value = {
        @ApiResponse(code = 201, message = "Stock exchange created successfully"),
        @ApiResponse(code = 400, message = "Invalid input or stock exchange already exists")
      })
  @PostMapping("/stock-exchange")
  public ResponseEntity<Object> createStockExchange(@RequestBody StockExchange stockExchange) {
    try {
      StockExchange createdExchange = stockExchangeService.createStockExchange(stockExchange);
      return new ResponseEntity<>(createdExchange, HttpStatus.CREATED);
    } catch (IllegalArgumentException e) {
      return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }
  }

  @ApiOperation(value = "Get a stock exchange by name", response = StockExchange.class)
  @ApiResponses(
      value = {
        @ApiResponse(code = 200, message = "Stock exchange found"),
        @ApiResponse(code = 404, message = "Stock exchange not found")
      })
  @GetMapping("/stock-exchange/{name}")
  public ResponseEntity<Object> getStockExchange(@PathVariable String name) {
    StockExchange stockExchange = stockExchangeService.getStockExchangeByName(name);
    if (stockExchange != null) {
      return new ResponseEntity<>(stockExchange, HttpStatus.OK);
    } else {
      return new ResponseEntity<>("Stock exchange not found", HttpStatus.NOT_FOUND);
    }
  }

  @ApiOperation(value = "Create a new stock", response = Stock.class)
  @ApiResponses(
      value = {
        @ApiResponse(code = 201, message = "Stock created successfully"),
        @ApiResponse(code = 400, message = "Invalid input")
      })
  @PostMapping("/stock")
  public ResponseEntity<Object> createStock(@RequestBody Stock stock) {

    try {
      Stock createdStock = stockExchangeService.createStock(stock);
      return new ResponseEntity<>(createdStock, HttpStatus.CREATED);
    } catch (IllegalArgumentException e) {
      return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }
  }

  @ApiOperation(value = "Add a stock to a stock exchange")
  @ApiResponses(
      value = {
        @ApiResponse(code = 204, message = "Stock added successfully"),
        @ApiResponse(code = 404, message = "Stock exchange or stock not found"),
        @ApiResponse(code = 400, message = "Invalid input")
      })
  @PostMapping("/stock-exchange/{name}/add-stock-by-id/{stockId}")
  public ResponseEntity<Object> addStockToExchangeById(
      @PathVariable String name, @PathVariable Long stockId) {
    try {
      stockExchangeService.addStockToExchangeById(name, stockId);
      return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    } catch (ResourceNotFoundException e) {
      return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
    } catch (IllegalArgumentException e) {
      return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }
  }

  @ApiOperation(value = "Add a stock to a stock exchange")
  @ApiResponses(
      value = {
        @ApiResponse(code = 204, message = "Stock added successfully"),
        @ApiResponse(code = 404, message = "Stock exchange or stock not found"),
        @ApiResponse(code = 400, message = "Invalid input")
      })
  @PostMapping("/stock-exchange/{name}/add-stock-by-name/{stockName}")
  public ResponseEntity<Object> addStockToExchangeByName(
      @PathVariable String name, @PathVariable String stockName) {
    try {
      stockExchangeService.addStockToExchangeByName(name, stockName);
      return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    } catch (ResourceNotFoundException e) {
      return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
    } catch (IllegalArgumentException e) {
      return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }
  }

  @ApiOperation(value = "Remove a stock from a stock exchange", response = StockExchange.class)
  @ApiResponses(
      value = {
        @ApiResponse(code = 200, message = "Stock removed successfully"),
        @ApiResponse(code = 404, message = "Stock exchange or stock not found"),
        @ApiResponse(code = 400, message = "Invalid input")
      })
  @PostMapping("/stock-exchange/{name}/remove-stock-by-id/{stockId}")
  public ResponseEntity<Object> removeStockFromExchangeById(
      @PathVariable String name, @PathVariable Long stockId) {
    try {
      stockExchangeService.removeStockFromExchangeById(name, stockId);
      return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    } catch (ResourceNotFoundException e) {
      return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
    } catch (IllegalArgumentException e) {
      return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }
  }

  @ApiOperation(value = "Remove a stock from a stock exchange", response = StockExchange.class)
  @ApiResponses(
      value = {
        @ApiResponse(code = 200, message = "Stock removed successfully"),
        @ApiResponse(code = 404, message = "Stock exchange or stock not found"),
        @ApiResponse(code = 400, message = "Invalid input")
      })
  @PostMapping("/stock-exchange/{name}/remove-stock-by-name/{stockName}")
  public ResponseEntity<Object> removeStockFromExchangeByName(
      @PathVariable String name, @PathVariable String stockName) {
    try {
      stockExchangeService.removeStockFromExchangeByName(name, stockName);
      return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    } catch (ResourceNotFoundException e) {
      return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
    } catch (IllegalArgumentException e) {
      return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }
  }

  @ApiOperation(value = "Update stock price", response = Stock.class)
  @ApiResponses(
      value = {
        @ApiResponse(code = 200, message = "Stock price updated successfully"),
        @ApiResponse(code = 404, message = "Stock not found")
      })
  @PutMapping("/stock/{id}/update-price")
  public ResponseEntity<Stock> updateStockPrice(
      @PathVariable Long id, @RequestBody Map<String, BigDecimal> request) {
    try {
      BigDecimal newPrice = request.get("newPrice");
      Stock updatedStock = stockExchangeService.updateStockPrice(id, newPrice);
      return new ResponseEntity<>(updatedStock, HttpStatus.OK);
    } catch (RuntimeException e) {
      return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }
  }

  @ApiOperation(value = "Delete a stock")
  @ApiResponses(
      value = {
        @ApiResponse(code = 204, message = "Stock deleted successfully"),
        @ApiResponse(code = 404, message = "Stock not found")
      })
  @DeleteMapping("/stock/{id}")
  public ResponseEntity<Void> deleteStock(@PathVariable Long id) {
    stockExchangeService.deleteStock(id);
    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }
}
