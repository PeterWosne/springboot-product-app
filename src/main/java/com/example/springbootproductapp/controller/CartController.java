package com.example.springbootproductapp.controller;

import com.example.springbootproductapp.service.ProductCart;
import com.example.springbootproductapp.service.ProductDTO;
import com.example.springbootproductapp.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/cart")
public class CartController {

    private ProductService productService;
    private ProductCart productCart;

    @Autowired
    public CartController(ProductService productService, ProductCart productCart) {
        this.productService = productService;
        this.productCart = productCart;
    }

    @GetMapping()
    public String listPage(Model model) {
        model.addAttribute("products", productCart.findAll());
        model.addAttribute("totalPrice", productCart.getTotalPrice());
        return "cart";
    }

    @PostMapping("/{id}")
    public String add(@PathVariable("id") Long id) {
        ProductDTO productDTO = productService.findById(id).orElseThrow(NotFoundException::new);
        productCart.add(productDTO);
        return "redirect:/catalog";
    }

    @PostMapping("/inc/{id}")
    public String addFromCart(@PathVariable("id") Long id) {
        ProductDTO productDTO = productService.findById(id).orElseThrow(NotFoundException::new);
        productCart.add(productDTO);
        return "redirect:/cart";
    }

    @PostMapping("/dec/{id}")
    public String removeFromCart(@PathVariable("id") Long id) {
        ProductDTO productDTO = productService.findById(id).orElseThrow(NotFoundException::new);
        productCart.decrement(productDTO);
        return "redirect:/cart";
    }

    @DeleteMapping("/{id}")
    public String removeProductTotal(@PathVariable("id") Long id) {
        ProductDTO productDTO = productService.findById(id).orElseThrow(NotFoundException::new);
        productCart.removeProduct(productDTO);
        return "redirect:/cart";
    }
}
