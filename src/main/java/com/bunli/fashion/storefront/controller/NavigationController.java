package com.bunli.fashion.storefront.controller;

import com.bunli.fashion.storefront.dto.*;
import com.bunli.fashion.storefront.service.StorefrontService;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class NavigationController {
    private final StorefrontService storefrontService;

    public NavigationController(StorefrontService storefrontService) { this.storefrontService = storefrontService; }

    @GetMapping("/navigation")
    public List<NavigationItemResponse> navigation() { return storefrontService.navigation(); }

    @GetMapping("/navigation/{slug}")
    public NavigationItemResponse navigationBySlug(@PathVariable String slug) { return storefrontService.navigationBySlug(slug); }

    @GetMapping("/categories")
    public List<CategoryResponse> categories() { return storefrontService.categories(); }

    @GetMapping("/categories/{slug}")
    public CategoryResponse categoryBySlug(@PathVariable String slug) { return storefrontService.categoryBySlug(slug); }

    @GetMapping("/categories/{slug}/children")
    public List<CategoryResponse> categoryChildren(@PathVariable String slug) { return storefrontService.categoryChildren(slug); }

    @GetMapping("/collections")
    public List<CollectionResponse> collections() { return storefrontService.collections(); }

    @GetMapping("/collections/{slug}")
    public CollectionResponse collectionBySlug(@PathVariable String slug) { return storefrontService.collectionBySlug(slug); }

    @GetMapping("/home")
    public HomeResponse home() { return storefrontService.home(); }

    @GetMapping("/banners")
    public List<BannerResponse> banners() { return storefrontService.banners(); }
}
