package com.bunli.fashion.storefront.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "categories")
public class Category extends ActiveEntity {
    @Column(name = "parent_id")
    private Long parentId;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private String slug;
    private String description;
    @Column(name = "image_id")
    private Long imageId;
    @Column(name = "sort_order")
    private int sortOrder;

    public Long getParentId() {
        return parentId;
    }

    public String getName() {
        return name;
    }

    public String getSlug() {
        return slug;
    }

    public String getDescription() {
        return description;
    }

    public Long getImageId() {
        return imageId;
    }

    public int getSortOrder() {
        return sortOrder;
    }
}
