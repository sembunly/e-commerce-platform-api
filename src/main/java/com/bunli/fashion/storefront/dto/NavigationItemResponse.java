package com.bunli.fashion.storefront.dto;

import java.util.List;

/**
 * Navigation tree node — suitable for rendering a mega menu in React.
 * <p>
 * Top-level items are the main menu tabs (Women, Men, New Arrivals,
 * Collections, Sale).
 * Children are sub-categories (Shoes, Bags, Accessories) or featured links.
 * {@code categorySlug} / {@code collectionSlug} allow the front-end to build
 * route paths without a secondary lookup.
 * </p>
 */
public record NavigationItemResponse(
                Long id,
                String name,
                String slug,
                Long categoryId,
                String categorySlug,
                Long collectionId,
                String collectionSlug,
                String url,
                int sortOrder,
                MediaResponse image,
                List<NavigationItemResponse> children) {
}
