package com.example.springbootproductapp.persist;

import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long>, JpaSpecificationExecutor<Product> {

    List<Product> findProductByPriceBetween(BigDecimal minPrice, BigDecimal maxPrice);
    List<Product> findAllByOrderByPriceAsc();
    List<Product> findAllByOrderByPriceDesc();
}

