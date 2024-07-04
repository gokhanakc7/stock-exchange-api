package com.stockexchange.api.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import java.util.Set;

@Entity
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class StockExchange {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String name;
  private String description;
  private boolean liveInMarket;

  @ManyToMany
  @JoinTable(
      name = "stock_exchange_stock",
      joinColumns = @JoinColumn(name = "stock_exchange_id"),
      inverseJoinColumns = @JoinColumn(name = "stock_id"))
  private Set<Stock> listedStocks;

  // Getters and Setters

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public boolean isLiveInMarket() {
    return liveInMarket;
  }

  public void setLiveInMarket(boolean liveInMarket) {
    this.liveInMarket = liveInMarket;
  }

  public Set<Stock> getListedStocks() {
    return listedStocks;
  }

  public void setListedStocks(Set<Stock> listedStocks) {
    this.listedStocks = listedStocks;
  }
}
