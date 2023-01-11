package com.example.springbootproductapp.service;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.Objects;

public class LineItem {

    @JsonIgnore
    private UserDTO user;

    private ProductDTO product;

    private Integer qty;

    public LineItem() {}

    public LineItem(ProductDTO product, UserDTO user, int qty) {
        this.product = product;
        this.user = user;
        this.qty = qty;
    }

    public ProductDTO getProduct() {
        return product;
    }

    public void setProduct(ProductDTO product) {
        this.product = product;
    }

    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
    }

    public Integer getQty() {
        return qty;
    }

    public void setQty(Integer qty) {
        this.qty = qty;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LineItem lineItem = (LineItem) o;
        return product.getId().equals(lineItem.product.getId()) && user.getId().equals(lineItem.user.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(product.getId(), user.getId());
    }
}
