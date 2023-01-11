package com.example.springbootproductapp.service;

import com.example.springbootproductapp.controller.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class CartServiceImpl implements CartService{

    private Map<Integer, Map<LineItem, Integer>> lineItemsMap = new ConcurrentHashMap<>();

    private ProductService productService;

    private UserService userService;

    @Autowired
    public CartServiceImpl(ProductService productService, UserService userService) {
        this.productService = productService;
        this.userService = userService;
    }

    @Override
    public void addProductForUserQty(int userId, long productId, int qty) {
        Map<LineItem, Integer> itemsForUser = lineItemsMap.computeIfAbsent(userId, k -> new HashMap<>());

        UserDTO user = userService.findById(userId).orElseThrow(NotFoundException::new);
        ProductDTO product = productService.findById(productId).orElseThrow(NotFoundException::new);
        LineItem lineItem = new LineItem(product,user, qty);

        itemsForUser.merge(lineItem,qty, Integer::sum);
    }

    @Override
    public void removeProductForUserQty(int userId, long productId, int qty) {
        Map<LineItem, Integer> itemsForUser = lineItemsMap.getOrDefault(userId, new HashMap<>());

        if(itemsForUser == null) {
            return;
        }

        ProductDTO product = new ProductDTO();
        product.setId(productId);
        UserDTO user = new UserDTO();
        user.setId(userId);
        LineItem lineItem = new LineItem(product, user, qty);

        if(!itemsForUser.containsKey(lineItem)) {
            return;
        }

        int count = itemsForUser.get(lineItem);
        if(qty >= count) {
            itemsForUser.remove(lineItem);
        }else {
            itemsForUser.put(lineItem, count - qty);
        }
    }

    @Override
    public void removeAllForUser(int userId) {
        lineItemsMap.remove(userId);
    }

    @Override
    public List<LineItem> findAllForUser(int userId) {
        List<LineItem> list = new ArrayList<>(lineItemsMap.get(userId).keySet());
        list.forEach(s -> s.setQty(lineItemsMap.get(userId).get(s)));
        return list;
    }
}
