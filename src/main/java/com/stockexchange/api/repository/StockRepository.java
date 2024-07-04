package com.stockexchange.api.repository;

import com.stockexchange.api.model.Stock;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StockRepository extends JpaRepository<Stock, Long> {
  Optional<Stock> findByName(String name);
}
