package com.example.springbootproductapp.persist;

import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;

public final class ProductSpecification {

    public static Specification<Product> productNameLake(String productName) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.like(root.get("title"), "%" + productName + "%");
    }

    public static Specification<Product> minPrice(BigDecimal minPrice) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.ge(root.get("price"), minPrice);
    }

    public static Specification<Product> maxPrice(BigDecimal maxPrice) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.le(root.get("price"), maxPrice);
    }
}
