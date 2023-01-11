package com.example.springbootproductapp.controller;

import com.example.springbootproductapp.service.ProductDTO;
import com.example.springbootproductapp.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/product")
public class ProductController {

    private ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
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
                size.orElse(5),
                page.orElse(1) - 1,
                sortField.filter(s -> !s.isBlank()).orElse(null)
        );


        model.addAttribute("products", products);
        return "product";
    }

    @GetMapping("/{id}")
    public String editPage(@PathVariable("id") Long id, Model model ) {
        model.addAttribute("product", productService.findById(id).orElseThrow(NotFoundException::new));
        return "product_form";
    }

    @PostMapping("/update")
    public String update(@Valid @ModelAttribute("product") ProductDTO productDTO, BindingResult bindingResult) {

        if(bindingResult.hasErrors()) {
            return "product_form";
        }

        productService.save(productDTO);
        return "redirect:/product";
    }

    @GetMapping("/new")
    public String create(Model model) {
        model.addAttribute("product", new ProductDTO());
        return "product_form";
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") Long id) {
        productService.delete(id);
        return "redirect:/product";
    }

    @ExceptionHandler
    public ModelAndView notFoundExceptionHandler(NotFoundException ex) {
        ModelAndView mav = new ModelAndView("not_found");
        mav.setStatus(HttpStatus.NOT_FOUND);
        return mav;
    }
}
