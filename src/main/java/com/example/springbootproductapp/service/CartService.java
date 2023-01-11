package com.example.springbootproductapp.service;

import java.util.List;

public interface CartService {

    void addProductForUserQty(int userId, long productId, int qty);

    void removeProductForUserQty(int userId, long productId, int qty);

    void removeAllForUser(int userId);

    List<LineItem> findAllForUser(int userId);
}
