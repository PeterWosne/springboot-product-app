package com.example.springbootproductapp.rest;

import com.example.springbootproductapp.controller.BadRequestException;
import com.example.springbootproductapp.controller.NotFoundException;
import com.example.springbootproductapp.service.ProductDTO;
import com.example.springbootproductapp.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/product")
public class ProductResource {

    private ProductService productService;

    @Autowired
    public ProductResource(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping(path = "/all", produces = "application/json") //produces = "application/json" это по дефолту
    public List<ProductDTO> findAll() {
        return productService.findAll();
    }

    @GetMapping(path = "/{id}", produces = "application/json")
    public ProductDTO findById(@PathVariable("id") Long id) {
        return productService.findById(id).orElseThrow(NotFoundException::new);
    }

    @PostMapping(produces = "application/json")
    public ProductDTO create(@RequestBody ProductDTO productDTO) {
        if(productDTO.getId() != null) {
            throw new BadRequestException();
        }
        productService.save(productDTO);
        return productDTO;
    }

    @PutMapping
    public void edit(@RequestBody ProductDTO productDTO) {
        if(productDTO.getId() == null) {
            throw new BadRequestException();
        }
        productService.save(productDTO);
    }

    @DeleteMapping(path = "/{id}")
    public void delete(@PathVariable("id") Long id) {
        productService.delete(id);
    }

    @ExceptionHandler
    public ResponseEntity<String> notFoundExceptionHandler(NotFoundException ex) {
        return new ResponseEntity<>("Entity not found", HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    public ResponseEntity<String> badRequestException(BadRequestException ex) {
        return new ResponseEntity<>("Bad request", HttpStatus.BAD_REQUEST);
    }
}

//этот класс будет контроллером, но контроллеры для REST принято называть ресорсами
//TODO если из json надо исключить некоторое поле из сущности то использовать @JsonIgnore


