package com.bunli.fashion.storefront.controller;

import com.bunli.fashion.storefront.dto.ProductPageResponse;
import com.bunli.fashion.storefront.service.StorefrontService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

/**
 * Public storefront product-section endpoints.
 *
 * <p>All endpoints return paginated {@link ProductPageResponse} objects with
 * enriched product cards (salePrice, imageUrl, onSale) ready for React TSX.</p>
 *
 * <ul>
 *   <li>GET /products/new-arrivals  — newest active products</li>
 *   <li>GET /products/featured      — is_featured = 1 products</li>
 *   <li>GET /products/best-sellers  — ordered by total units sold (non-cancelled orders)</li>
 *   <li>GET /products/sale          — products with at least one variant with sale_price &lt; price</li>
 * </ul>
 *
 * <p>Query params: {@code page} (zero-based, default 0) and {@code size} (default 12, max 50).</p>
 */
@Tag(name = "Product Sections", description = "Public homepage product sections — no authentication required")
@RestController
@RequestMapping("/api/v1/products")
public class ProductSectionController {

    private final StorefrontService storefrontService;

    public ProductSectionController(StorefrontService storefrontService) {
        this.storefrontService = storefrontService;
    }

    @Operation(summary = "New Arrivals",
               description = "Returns the latest active products ordered by created_at descending. "
                       + "Includes variant-resolved sale price and primary product image URL.")
    @GetMapping("/new-arrivals")
    public ProductPageResponse newArrivals(
            @Parameter(description = "Zero-based page number") @RequestParam(defaultValue = "0")  int page,
            @Parameter(description = "Items per page (max 50)")@RequestParam(defaultValue = "12") int size) {
        return storefrontService.newArrivals(page, size);
    }

    @Operation(summary = "Featured products",
               description = "Returns products where is_featured = 1, ordered by created_at descending.")
    @GetMapping("/featured")
    public ProductPageResponse featured(
            @RequestParam(defaultValue = "0")  int page,
            @RequestParam(defaultValue = "12") int size) {
        return storefrontService.featured(page, size);
    }

    @Operation(summary = "Best Sellers",
               description = "Returns products ordered by total units sold across non-cancelled orders. "
                       + "Products with no sales history are excluded.")
    @GetMapping("/best-sellers")
    public ProductPageResponse bestSellers(
            @RequestParam(defaultValue = "0")  int page,
            @RequestParam(defaultValue = "12") int size) {
        return storefrontService.bestSellers(page, size);
    }

    @Operation(summary = "Sale products",
               description = "Returns products that have at least one active variant with "
                       + "sale_price < price. Ordered by created_at descending.")
    @GetMapping("/sale")
    public ProductPageResponse sale(
            @RequestParam(defaultValue = "0")  int page,
            @RequestParam(defaultValue = "12") int size) {
        return storefrontService.sale(page, size);
    }
}
