package com.bunli.fashion.storefront.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "product_images")
public class ProductImage extends ActiveEntity {
    @Column(name = "product_id", nullable = false) private Long productId;
    @Column(name = "media_id", nullable = false) private Long mediaId;
    @Column(name = "color_id") private Long colorId;
    @Column(name = "sort_order") private int sortOrder;
    @Column(name = "alt_text") private String altText;
    public Long getProductId() { return productId; }
    public Long getMediaId() { return mediaId; }
    public Long getColorId() { return colorId; }
    public int getSortOrder() { return sortOrder; }
    public String getAltText() { return altText; }
}
