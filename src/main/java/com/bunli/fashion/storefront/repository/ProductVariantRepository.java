package com.bunli.fashion.storefront.repository;

import com.bunli.fashion.storefront.entity.ProductVariant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.List;
import java.util.Collection;

public interface ProductVariantRepository extends JpaRepository<ProductVariant, Long> {

    interface CardPriceView {
        Long getProductId();

        BigDecimal getPrice();

        BigDecimal getSalePrice();
    }

    @Query(value = """
            select pv.product_id as productId, min(pv.price) as price,
                   min(case when pv.sale_price is not null and pv.sale_price < pv.price then pv.sale_price end) as salePrice
            from product_variants pv where pv.is_active = 1 and pv.product_id in (:productIds)
            group by pv.product_id
            """, nativeQuery = true)
    List<CardPriceView> findCardPricesByProductIds(@Param("productIds") Collection<Long> productIds);

    interface VariantView {
        Long getId();

        String getSku();

        BigDecimal getPrice();

        BigDecimal getSalePrice();

        int getStockQuantity();

        int getReservedQuantity();

        Long getSizeId();

        String getSizeName();

        Long getColorId();

        String getColorName();

        String getColorHexCode();
    }

    @Query(value = """
            select pv.id, pv.sku, pv.price, pv.sale_price as salePrice, pv.stock_quantity as stockQuantity,
                   pv.reserved_quantity as reservedQuantity, s.id as sizeId, s.name as sizeName,
                   c.id as colorId, c.name as colorName, c.hex_code as colorHexCode
            from product_variants pv join colors c on c.id = pv.color_id and c.is_active = 1
            left join sizes s on s.id = pv.size_id and s.is_active = 1
            where pv.product_id = :productId and pv.is_active = 1 order by c.name, s.sort_order, pv.id
            """, nativeQuery = true)
    List<VariantView> findActiveViewsByProductId(@Param("productId") Long productId);

    @Query("""
            select min(pv.salePrice) from ProductVariant pv
            where pv.productId = :productId
              and pv.active = :active
              and pv.salePrice is not null
              and pv.salePrice < pv.price
            """)
    Optional<BigDecimal> findMinSalePriceByProductId(@Param("productId") Long productId,
            @Param("active") Byte active);

    /**
     * Returns the lowest base price among active variants of a product.
     */
    @Query("""
            select min(pv.price) from ProductVariant pv
            where pv.productId = :productId
              and pv.active = :active
            """)
    Optional<BigDecimal> findMinPriceByProductId(@Param("productId") Long productId,
            @Param("active") Byte active);
}
