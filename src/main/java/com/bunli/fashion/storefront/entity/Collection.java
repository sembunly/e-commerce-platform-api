package com.bunli.fashion.storefront.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "collections")
public class Collection extends ActiveEntity {
    @Column(nullable = false) private String name;
    @Column(nullable = false) private String slug;
    private String description;
    @Column(name = "image_id") private Long imageId;
    public String getName() { return name; }
    public String getSlug() { return slug; }
    public String getDescription() { return description; }
    public Long getImageId() { return imageId; }
}
