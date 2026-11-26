package com.bunli.fashion.storefront.controller;

import com.bunli.fashion.storefront.dto.*;
import com.bunli.fashion.storefront.service.StorefrontService;
import java.math.BigDecimal;
import java.util.List;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
public class CatalogController {
        private final StorefrontService storefrontService;

        public CatalogController(StorefrontService storefrontService) {
                this.storefrontService = storefrontService;
        }

        @GetMapping("/products")
        public ProductPageResponse products(@RequestParam(required = false) String keyword,
                        @RequestParam(required = false) String gender, @RequestParam(required = false) String category,
                        @RequestParam(required = false) String collection, @RequestParam(required = false) String size,
                        @RequestParam(required = false) String color,
                        @RequestParam(required = false) BigDecimal minPrice,
                        @RequestParam(required = false) BigDecimal maxPrice,
                        @RequestParam(required = false) Boolean inStock,
                        @RequestParam(defaultValue = "newest") String sort, @RequestParam(defaultValue = "0") int page,
                        @RequestParam(name = "sizePerPage", defaultValue = "12") int sizePerPage) {
                return storefrontService.catalog(keyword, gender, category, collection, size, color, minPrice, maxPrice,
                                inStock, sort, page, sizePerPage);
        }

        @GetMapping("/products/search")
        public ProductPageResponse search(@RequestParam String keyword,
                        @RequestParam(defaultValue = "newest") String sort,
                        @RequestParam(defaultValue = "0") int page,
                        @RequestParam(name = "sizePerPage", defaultValue = "12") int sizePerPage) {
                return storefrontService.catalog(keyword, null, null, null, null, null, null, null, null, sort, page,
                                sizePerPage);
        }

        @GetMapping("/products/filters")
        public ProductFiltersResponse filters() {
                return storefrontService.filters();
        }

        @GetMapping("/products/{slug}")
        public ProductDetailResponse product(@PathVariable String slug) {
                return storefrontService.productBySlug(slug);
        }

        @GetMapping("/products/{slug}/variants")
        public List<VariantResponse> variants(@PathVariable String slug) {
                return storefrontService.productVariants(slug);
        }

        @GetMapping("/products/{slug}/related")
        public ProductPageResponse related(@PathVariable String slug, @RequestParam(defaultValue = "0") int page,
                        @RequestParam(defaultValue = "8") int size) {
                return storefrontService.relatedProducts(slug, page, size);
        }

        @GetMapping("/categories/{slug}/products")
        public ProductPageResponse categoryProducts(@PathVariable String slug,
                        @RequestParam(required = false) String keyword,
                        @RequestParam(required = false) String gender, @RequestParam(required = false) String size,
                        @RequestParam(required = false) String color,
                        @RequestParam(required = false) BigDecimal minPrice,
                        @RequestParam(required = false) BigDecimal maxPrice,
                        @RequestParam(required = false) Boolean inStock,
                        @RequestParam(defaultValue = "newest") String sort, @RequestParam(defaultValue = "0") int page,
                        @RequestParam(name = "sizePerPage", defaultValue = "12") int sizePerPage) {
                return storefrontService.categoryProducts(slug, keyword, gender, size, color, minPrice, maxPrice,
                                inStock,
                                sort, page, sizePerPage);
        }

        @GetMapping("/collections/{slug}/products")
        public ProductPageResponse collectionProducts(@PathVariable String slug,
                        @RequestParam(required = false) String keyword,
                        @RequestParam(required = false) String gender, @RequestParam(required = false) String size,
                        @RequestParam(required = false) String color,
                        @RequestParam(required = false) BigDecimal minPrice,
                        @RequestParam(required = false) BigDecimal maxPrice,
                        @RequestParam(required = false) Boolean inStock,
                        @RequestParam(defaultValue = "newest") String sort, @RequestParam(defaultValue = "0") int page,
                        @RequestParam(name = "sizePerPage", defaultValue = "12") int sizePerPage) {
                return storefrontService.collectionProducts(slug, keyword, gender, size, color, minPrice, maxPrice,
                                inStock,
                                sort, page, sizePerPage);
        }

        @GetMapping("/stores")
        public List<StoreResponse> stores() {
                return storefrontService.stores();
        }

        @GetMapping("/stores/{id}")
        public StoreResponse store(@PathVariable Long id) {
                return storefrontService.store(id);
        }

        @GetMapping("/articles")
        public ArticlePageResponse articles(@RequestParam(defaultValue = "0") int page,
                        @RequestParam(defaultValue = "12") int size) {
                return storefrontService.articles(page, size);
        }

        @GetMapping("/articles/{slug}")
        public ArticleDetailResponse article(@PathVariable String slug) {
                return storefrontService.articleBySlug(slug);
        }
}
