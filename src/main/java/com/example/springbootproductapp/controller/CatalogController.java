package com.example.springbootproductapp.controller;

import com.example.springbootproductapp.service.ProductCart;
import com.example.springbootproductapp.service.ProductDTO;
import com.example.springbootproductapp.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.util.Optional;

@Controller
@RequestMapping("/catalog")
public class CatalogController {

    private ProductService productService;

    private ProductCart productCart;

    @Autowired
    public CatalogController(ProductService productService, ProductCart productCart) {
        this.productService = productService;
        this.productCart = productCart;
    }

    @GetMapping
    public String listPage(Model model, HttpSession session,
                           @RequestParam("productnameFilter") Optional<String> productnameFilter,
                           @RequestParam("minPriceFilter") Optional<BigDecimal> minPriceFilter,
                           @RequestParam("maxPriceFilter") Optional<BigDecimal> maxPriceFilter,
                           @RequestParam("page") Optional<Integer> page,
                           @RequestParam("size") Optional<Integer> size,
                           @RequestParam("sortField") Optional<String> sortField) {

        Page<ProductDTO> products = productService.findWithFilter(
                productnameFilter.filter(s -> !s.isBlank()).orElse(null),
                minPriceFilter.orElse(null),
                maxPriceFilter.orElse(null),
                size.orElse(8),
                page.orElse(1) - 1,
                sortField.filter(s -> !s.isBlank()).orElse(null)
        );


        model.addAttribute("products", products);
        return "catalog";
    }
}
