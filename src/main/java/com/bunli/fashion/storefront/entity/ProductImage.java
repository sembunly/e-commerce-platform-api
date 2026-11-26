package com.bunli.fashion.storefront.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "product_images")
public class ProductImage extends ActiveEntity {
    @Column(name = "product_id", nullable = false) private Long productId;
    @Column(name = "media_id", nullable = false) private Long mediaId;
    @Column(name = "sort_order") private int sortOrder;
    public Long getProductId() { return productId; }
    public Long getMediaId() { return mediaId; }
    public int getSortOrder() { return sortOrder; }
}
