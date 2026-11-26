package com.bunli.fashion.storefront.dto;

import java.util.List;

/**
 * Navigation tree node — suitable for rendering a mega menu in React.
 * <p>
 * Top-level items are the main menu tabs (Women, Men, New Arrivals, Collections, Sale).
 * Children are sub-categories (Shoes, Bags, Accessories) or featured links.
 * {@code categorySlug} / {@code collectionSlug} allow the front-end to build
 * route paths without a secondary lookup.
 * </p>
 */
public record NavigationItemResponse(
        Long   id,
        String name,
        String slug,
        Long   categoryId,
        /** Resolved slug of the linked category — ready for React Router {@code /categories/:slug} */
        String categorySlug,
        Long   collectionId,
        /** Resolved slug of the linked collection — ready for React Router {@code /collections/:slug} */
        String collectionSlug,
        String url,
        int    sortOrder,
        /** Cover image of the linked category or collection (null for plain-link items) */
        MediaResponse image,
        List<NavigationItemResponse> children
) {}
