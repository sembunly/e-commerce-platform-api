package com.bunli.fashion.storefront.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "sizes")
public class Size extends ActiveEntity {
    @Column(nullable = false)
    private String name;
    @Column(name = "sort_order")
    private int sortOrder;

    public String getName() {
        return name;
    }

    public int getSortOrder() {
        return sortOrder;
    }
}
