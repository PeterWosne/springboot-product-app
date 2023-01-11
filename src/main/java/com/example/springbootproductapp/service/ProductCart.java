package com.example.springbootproductapp.service;

import com.example.springbootproductapp.service.ProductDTO;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


@Data
@Component
@NoArgsConstructor
@SessionScope
public class ProductCart {

    private Map<ProductDTO, Integer> productMap = new ConcurrentHashMap<>();

    private Double totalPrice = 0.0;

    public void add(ProductDTO productDTO) {
        if(!productMap.containsKey(productDTO)) {
            productMap.put(productDTO, 1);
        }else {
            productMap.put(productDTO, productMap.get(productDTO) + 1);
        }

        totalPrice += productDTO.getPrice().doubleValue();
    }

    public Map<ProductDTO, Integer> findAll() {
        return productMap;
    }

    public void removeProduct(ProductDTO productDTO) {
        totalPrice = totalPrice - productDTO.getPrice().doubleValue() * productMap.get(productDTO);
        productMap.remove(productDTO);
    }

    public void decrement(ProductDTO productDTO) {
        if(productMap.get(productDTO) <= 1) {
            productMap.remove(productDTO);
        }else {
            productMap.put(productDTO, productMap.get(productDTO) - 1);
        }
        totalPrice -= productDTO.getPrice().doubleValue();
    }

    public void clear() {}
}

//@SessionScope означает, что этот бин имеет область видимости Session, то есть  каждый раз для новой сессии создается новый экземпляр ProductCart
//Этот бин не удаляется, пока сессия пользователя не истечет.
