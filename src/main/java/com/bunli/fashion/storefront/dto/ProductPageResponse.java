package com.bunli.fashion.storefront.dto;

import java.util.List;

/**
 * Paginated product listing — used by /products/new-arrivals, /featured,
 * /best-sellers, and /sale endpoints.
 */
public record ProductPageResponse(
        List<ProductSummaryResponse> items,
        int  page,
        int  size,
        long totalElements,
        int  totalPages,
        /** True when a next page exists */
        boolean hasNext,
        /** True when a previous page exists */
        boolean hasPrevious
) {}
