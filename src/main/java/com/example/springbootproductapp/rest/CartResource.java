package com.example.springbootproductapp.rest;

import com.example.springbootproductapp.service.CartService;
import com.example.springbootproductapp.service.LineItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/cart")
public class CartResource {

    private CartService cartService;

    @Autowired
    public CartResource(CartService cartService) {
        this.cartService = cartService;
    }

    @PostMapping("/{userId}/user/{productId}/product")
    public void addProductForUser(@PathVariable("userId") Integer userId,
                                  @PathVariable("productId") Long productId,
                                  @RequestParam("qty") Integer qty) {
        cartService.addProductForUserQty(userId, productId, qty);
    }

    @PutMapping("/{userId}/user/{productId}/product")
    public void removeProductForUser(@PathVariable("userId") Integer userId,
                                     @PathVariable("productId") Long productId,
                                     @RequestParam("qty") Integer qty) {
        cartService.removeProductForUserQty(userId, productId, qty);
    }

    @DeleteMapping("/{userId}/user")
    public void addAllForUser(@PathVariable("userId") Integer userId) {
        cartService.removeAllForUser(userId);
    }

    @GetMapping(path = "/{userId}/user", produces = "application/json")
    public List<LineItem> findProductsForUser(@PathVariable("userId") Integer userId) {
        return cartService.findAllForUser(userId);
    }
}
