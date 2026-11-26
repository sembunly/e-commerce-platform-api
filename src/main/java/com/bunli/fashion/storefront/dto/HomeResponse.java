package com.bunli.fashion.storefront.dto;

import java.util.List;

/**
 * Full homepage payload — one single GET /home call hydrates the entire
 * storefront.
 * <p>
 * Layout order mirrors 's storefront:
 * <ol>
 * <li>{@code heroBanners} — full-width hero carousel</li>
 * <li>{@code promotionalBanners} — secondary promotional strip</li>
 * <li>{@code featuredCollections} — collection grid / horizontal scroll</li>
 * <li>{@code navigation} — mega-menu tree (Women / Men / …)</li>
 * <li>{@code categories} — flat+nested category tree</li>
 * <li>{@code sections} — product sections (new-arrivals, featured,
 * best-sellers, sale)</li>
 * </ol>
 * </p>
 */

public record HomeResponse(
        List<NavigationItemResponse> navigation,
        /** Hero carousel banners (sort_order < 100) */
        List<BannerResponse> heroBanners,
        /** Promotional strip banners (sort_order >= 100) */
        List<BannerResponse> promotionalBanners,
        List<CategoryResponse> categories,
        /** Featured collections for the homepage grid */
        List<CollectionResponse> featuredCollections,
        List<ProductSectionResponse> sections) {
}
