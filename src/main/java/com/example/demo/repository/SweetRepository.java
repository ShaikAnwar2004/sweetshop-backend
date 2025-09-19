package com.example.demo.repository;

import com.example.demo.model.Sweet;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface SweetRepository extends JpaRepository<Sweet, Long> {

  List<Sweet> findByNameContainingIgnoreCase(String name);

  List<Sweet> findByCategoryIgnoreCase(String category);

  List<Sweet> findByPriceBetween(double minPrice, double maxPrice);
}
