package com.bunli.fashion.storefront.dto;

import java.util.List;

/**
 * Category tree node with nested children and optional cover image.
 * <p>
 * Top-level (parentId == null): Women, Men, New Arrivals, Sale.
 * Second-level: Shoes, Bags, Accessories.
 * </p>
 */
public record CategoryResponse(
                Long id,
                Long parentId,
                String name,
                String slug,
                String description,
                MediaResponse image,
                int sortOrder,
                List<CategoryResponse> children) {
}
