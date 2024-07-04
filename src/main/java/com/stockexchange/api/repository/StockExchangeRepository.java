package com.stockexchange.api.repository;

import com.stockexchange.api.model.StockExchange;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StockExchangeRepository extends JpaRepository<StockExchange, Long> {
  Optional<StockExchange> findByName(String name);

  List<StockExchange> findAll();

  boolean existsByName(String name);
}
