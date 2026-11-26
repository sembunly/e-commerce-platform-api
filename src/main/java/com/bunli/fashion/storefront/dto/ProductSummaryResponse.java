package com.bunli.fashion.storefront.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Compact product card for lists, sections, and search results.
 * <p>
 * {@code salePrice} is the lowest active variant sale_price (null when not on sale).
 * {@code onSale} is a derived boolean convenience field for React conditional rendering.
 * {@code imageUrl} duplicates {@code image.url} at top level for quick access.
 * </p>
 */
public record ProductSummaryResponse(
        Long          id,
        String        name,
        String        slug,
        String        description,
        BigDecimal    basePrice,
        /** Lowest active-variant price (may differ from basePrice when variants exist) */
        BigDecimal    price,
        /** Lowest active-variant sale_price; null when product is not on sale */
        BigDecimal    salePrice,
        /** true when at least one active variant has a sale_price < price */
        boolean       onSale,
        String        gender,
        String        material,
        boolean       featured,
        MediaResponse image,
        /** Convenience top-level URL: image.url or null */
        String        imageUrl,
        LocalDateTime createdAt
) {}
