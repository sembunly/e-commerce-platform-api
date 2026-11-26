package com.bunli.fashion.storefront.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "products")
public class Product extends ActiveEntity {
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private String slug;
    private String description;
    @Column(name = "base_price", nullable = false)
    private BigDecimal basePrice;
    private String gender;
    private String material;
    @Column(name = "is_featured", nullable = false)
    private Byte featured;
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    public String getName() {
        return name;
    }

    public String getSlug() {
        return slug;
    }

    public String getDescription() {
        return description;
    }

    public BigDecimal getBasePrice() {
        return basePrice;
    }

    public String getGender() {
        return gender;
    }

    public String getMaterial() {
        return material;
    }

    public boolean isFeatured() {
        return featured != null && featured == 1;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
