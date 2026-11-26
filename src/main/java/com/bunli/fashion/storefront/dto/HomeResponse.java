package com.bunli.fashion.storefront.dto;

import java.util.List;

public record HomeResponse(List<NavigationItemResponse> navigation, List<BannerResponse> banners,
                           List<CategoryResponse> categories, List<CollectionResponse> collections,
                           List<ProductSectionResponse> sections) {}
