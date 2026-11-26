package com.bunli.fashion.storefront.dto;

import java.util.List;

/**
 * Homepage product section — e.g. "New Arrivals", "Best Sellers", "Sale".
 * <p>
 * {@code key} is a stable identifier for React list rendering (e.g. "new-arrivals").
 * {@code viewAllUrl} is a ready-to-use href the React component can pass to a
 * "View All" button without constructing the URL itself.
 * </p>
 */
public record ProductSectionResponse(
        String key,
        String title,
        /** Pre-built relative URL for the "View All" CTA, e.g. "/products/new-arrivals" */
        String viewAllUrl,
        List<ProductSummaryResponse> products
) {}
