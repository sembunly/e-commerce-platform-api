package com.bunli.fashion.storefront.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "navigation_items")
public class NavigationItem extends ActiveEntity {
    @Column(name = "parent_id")
    private Long parentId;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private String slug;
    @Column(name = "category_id")
    private Long categoryId;
    @Column(name = "collection_id")
    private Long collectionId;
    private String url;
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

    public Long getCategoryId() {
        return categoryId;
    }

    public Long getCollectionId() {
        return collectionId;
    }

    public String getUrl() {
        return url;
    }

    public int getSortOrder() {
        return sortOrder;
    }
}
