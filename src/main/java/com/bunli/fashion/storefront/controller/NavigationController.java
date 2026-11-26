package com.bunli.fashion.storefront.controller;

import com.bunli.fashion.storefront.dto.*;
import com.bunli.fashion.storefront.service.StorefrontService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.springframework.web.bind.annotation.*;

/**
 * Public storefront endpoints — Navigation & Homepage.
 *
 * <ul>
 * <li>GET /navigation — full mega-menu tree (Women, Men, New Arrivals,
 * Collections, Sale)</li>
 * <li>GET /navigation/{slug} — single nav item with its children</li>
 * <li>GET /categories — full category tree with nested Shoes / Bags /
 * Accessories</li>
 * <li>GET /categories/{slug} — single category with children</li>
 * <li>GET /categories/{slug}/children — direct children of a category</li>
 * <li>GET /collections — all active collections</li>
 * <li>GET /collections/{slug} — single collection</li>
 * <li>GET /home — full homepage payload (banners + sections + navigation)</li>
 * <li>GET /banners — currently active banners</li>
 * </ul>
 */
@Tag(name = "Navigation & Homepage", description = "Public storefront endpoints — no authentication required")
@Tag(name = "Navigation & Homepage", description = "Public storefront endpoints — no authentication required")
@RestController
@RequestMapping("/api/v1")
public class NavigationController {

    private final StorefrontService storefrontService;

    public NavigationController(StorefrontService storefrontService) {
        this.storefrontService = storefrontService;
    }

    // ── Navigation ────────────────────────────────────────────────────────

    @Operation(summary = "Get full mega-menu navigation tree", description = "Returns all active navigation items in a tree structure. "
            + "Top-level items map to main tabs (Women, Men, New Arrivals, Collections, Sale). "
            + "Children contain nested category/sub-category links enriched with "
            + "categorySlug, collectionSlug, and cover images.")
    @GetMapping("/navigation")
    public List<NavigationItemResponse> navigation() {
        return storefrontService.navigation();
    }

    @Operation(summary = "Get a single navigation item by slug", description = "Returns one navigation item and all its active children.")
    @GetMapping("/navigation/{slug}")
    public NavigationItemResponse navigationBySlug(
            @Parameter(description = "Navigation item slug, e.g. 'women'") @PathVariable String slug) {
        return storefrontService.navigationBySlug(slug);
    }

    // ── Categories ────────────────────────────────────────────────────────

    @Operation(summary = "Get the full category tree", description = "Returns active categories nested to arbitrary depth. "
            + "Top-level: Women, Men, New Arrivals, Sale. "
            + "Second-level: Shoes, Bags, Accessories.")
    @GetMapping("/categories")
    public List<CategoryResponse> categories() {
        return storefrontService.categories();
    }

    @Operation(summary = "Get a single category by slug with its children")
    @GetMapping("/categories/{slug}")
    public CategoryResponse categoryBySlug(
            @Parameter(description = "Category slug, e.g. 'women-shoes'") @PathVariable String slug) {
        return storefrontService.categoryBySlug(slug);
    }

    @Operation(summary = "Get the direct children of a category", description = "Returns only the immediate sub-categories (without further nesting).")
    @GetMapping("/categories/{slug}/children")
    public List<CategoryResponse> categoryChildren(
            @Parameter(description = "Parent category slug, e.g. 'women'") @PathVariable String slug) {
        return storefrontService.categoryChildren(slug);
    }

    // ── Collections ───────────────────────────────────────────────────────

    @Operation(summary = "Get all active collections", description = "Returns every active collection with cover image and slug, "
            + "ordered alphabetically. Used in the Collections mega-menu tab "
            + "and the homepage featured-collections row.")
    @GetMapping("/collections")
    public List<CollectionResponse> collections() {
        return storefrontService.collections();
    }

    @Operation(summary = "Get a single collection by slug")
    @GetMapping("/collections/{slug}")
    public CollectionResponse collectionBySlug(
            @Parameter(description = "Collection slug, e.g. 'summer-24'") @PathVariable String slug) {
        return storefrontService.collectionBySlug(slug);
    }

    // ── Homepage ─────────────────────────────────────────────────────────

    @Operation(summary = "Get the full homepage payload", description = "Single call that hydrates the entire storefront homepage: "
            + "navigation tree, hero banners, promotional banners, "
            + "featured collections, category tree, and product sections "
            + "(new-arrivals, featured, best-sellers, sale — 8 products each).")
    @GetMapping("/home")
    public HomeResponse home() {
        return storefrontService.home();
    }

    // ── Banners ───────────────────────────────────────────────────────────

    @Operation(summary = "Get all currently active and scheduled banners", description = "Returns banners where is_active = 1 and the current time is "
            + "within [starts_at, ends_at]. Ordered by sort_order ascending. "
            + "Banners with sort_order < 100 are hero banners; >= 100 are promotional.")
    @GetMapping("/banners")
    public List<BannerResponse> banners() {
        return storefrontService.banners();
    }
}
