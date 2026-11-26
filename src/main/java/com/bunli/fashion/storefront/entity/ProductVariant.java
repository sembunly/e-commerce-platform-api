package com.bunli.fashion.storefront.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "product_variants")
public class ProductVariant extends ActiveEntity {
    @Column(name = "product_id", nullable = false) private Long productId;
    @Column(name = "size_id")                       private Long sizeId;
    @Column(name = "color_id", nullable = false)    private Long colorId;
    @Column(nullable = false)                       private String sku;
    @Column(nullable = false)                       private BigDecimal price;
    @Column(name = "sale_price")                    private BigDecimal salePrice;
    @Column(name = "stock_quantity")                private int stockQuantity;
    @Column(name = "reserved_quantity")             private int reservedQuantity;

    public Long getProductId()       { return productId; }
    public Long getSizeId()          { return sizeId; }
    public Long getColorId()         { return colorId; }
    public String getSku()           { return sku; }
    public BigDecimal getPrice()     { return price; }
    public BigDecimal getSalePrice() { return salePrice; }
    public int getStockQuantity()    { return stockQuantity; }
    public int getReservedQuantity() { return reservedQuantity; }
}
