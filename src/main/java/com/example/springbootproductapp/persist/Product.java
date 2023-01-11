package com.example.springbootproductapp.persist;

import com.example.springbootproductapp.service.ProductDTO;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 512, nullable = false)
    private String title;

    @Column(length = 512)
    private String description;

    @Column(nullable = false)
    private BigDecimal price;

    @Column(length=512, nullable = false)
    private String imageUrl;

    public Product() {}

    public Product(String title) {
        this.title = title;
    }

    public Product(ProductDTO productDTO) {
        this.id = productDTO.getId();
        this.title = productDTO.getTitle();
        this.description = productDTO.getDescription();
        this.price = productDTO.getPrice();
        this.imageUrl = productDTO.getImageUrl();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
