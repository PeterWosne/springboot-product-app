package com.example.springbootproductapp.service;

import com.example.springbootproductapp.persist.Product;
import com.example.springbootproductapp.persist.ProductRepository;
import com.example.springbootproductapp.persist.ProductSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService{

    private ProductRepository productRepository;
    protected Boolean isAscending = true;

    @Autowired
    public ProductServiceImpl(ProductRepository productRepository) {this.productRepository = productRepository;}

    @Override
    public Page<ProductDTO> findWithFilter(String productNameFilter, BigDecimal minPrice, BigDecimal maxPrice, Integer size, Integer page, String sort) {
        Specification<Product> spec = Specification.where(null);
        if(productNameFilter != null && !productNameFilter.isBlank()) {
            spec = spec.and(ProductSpecification.productNameLake(productNameFilter));
        }
        if(minPrice != null) {
            spec = spec.and(ProductSpecification.minPrice(minPrice));
        }
        if(maxPrice != null) {
            spec = spec.and(ProductSpecification.maxPrice(maxPrice));
        }

        if(sort != null && !sort.isBlank()) {
            String[] sorts = sort.split("_");
            if(sorts[1].equals("asc")) {
                return productRepository.findAll(spec, PageRequest.of(page,size, Sort.by(sorts[0]).ascending()))
                        .map(ProductDTO::new);
            }else {
                return productRepository.findAll(spec, PageRequest.of(page,size, Sort.by(sorts[0]).descending()))
                        .map(ProductDTO::new);
            }
        }

        return productRepository.findAll(spec, PageRequest.of(page,size))
                .map(ProductDTO::new);
    }

    @Transactional
    @Override
    public List<ProductDTO> findAll() {
        return productRepository.findAll().stream()
                .map(ProductDTO::new)
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public Optional<ProductDTO> findById(Long id) {
        return productRepository.findById(id)
                .map(ProductDTO::new);
    }

    @Override
    public List<ProductDTO> findWithFilterMinToMax(BigDecimal minPriceFilter, BigDecimal maxPriceFilter) {
        return productRepository.findProductByPriceBetween(minPriceFilter,maxPriceFilter).stream()
                .map(ProductDTO::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<ProductDTO> findWithFilterAsc() {
        return productRepository.findAllByOrderByPriceAsc().stream()
                .map(ProductDTO::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<ProductDTO> findWithFilterDesc() {
        return productRepository.findAllByOrderByPriceDesc().stream()
                .map(ProductDTO::new)
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public void save(ProductDTO productDTO) {
        Product productToSave = new Product(productDTO);
        productRepository.save(productToSave);
        productDTO.setId(productToSave.getId());
    }

    @Transactional
    @Override
    public void delete(Long id) {
        productRepository.deleteById(id);
    }
}

//TODO#1 аннотация @Service
//@Service - (Сервис-слой приложения) Аннотация, объявляющая, что этот класс
//представляет собой сервис –компонент сервис-слоя. Сервис является подтипом класса @Component.
//Использование данной аннотации позволит искать бины-сервисы автоматически
//TODO#2 транзакционность выносим на сервисный уровень
//TODO#3 конструктор с Autowired