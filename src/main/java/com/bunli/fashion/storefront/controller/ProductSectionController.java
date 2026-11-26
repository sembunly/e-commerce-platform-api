package com.bunli.fashion.storefront.controller;

import com.bunli.fashion.storefront.dto.ProductPageResponse;
import com.bunli.fashion.storefront.service.StorefrontService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/products")
public class ProductSectionController {
    private final StorefrontService storefrontService;
    public ProductSectionController(StorefrontService storefrontService) { this.storefrontService = storefrontService; }

    @GetMapping("/new-arrivals")
    public ProductPageResponse newArrivals(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "12") int size) {
        return storefrontService.newArrivals(page, size);
    }

    @GetMapping("/featured")
    public ProductPageResponse featured(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "12") int size) {
        return storefrontService.featured(page, size);
    }

    @GetMapping("/best-sellers")
    public ProductPageResponse bestSellers(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "12") int size) {
        return storefrontService.bestSellers(page, size);
    }

    @GetMapping("/sale")
    public ProductPageResponse sale(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "12") int size) {
        return storefrontService.sale(page, size);
    }
}
