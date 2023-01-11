package com.example.springbootproductapp.service;

import com.example.springbootproductapp.persist.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface ProductService {

    Page<ProductDTO> findWithFilter(String productNameFilter,
                                    BigDecimal minPrice,
                                    BigDecimal maxPrice,
                                    Integer size,
                                    Integer page,
                                    String sort);

    List<ProductDTO> findAll();
    Optional<ProductDTO> findById(Long id);

    List<ProductDTO> findWithFilterMinToMax(BigDecimal minPriceFilter, BigDecimal maxPriceFilter);
    List<ProductDTO> findWithFilterAsc();
    List<ProductDTO> findWithFilterDesc();

    void save(ProductDTO productDTO);
    void delete(Long id);
}
