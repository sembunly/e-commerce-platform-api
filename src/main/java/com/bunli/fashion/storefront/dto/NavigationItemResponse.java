package com.bunli.fashion.storefront.dto;

import java.util.List;

public record NavigationItemResponse(Long id, String name, String slug, Long categoryId, Long collectionId,
                                     String url, int sortOrder, List<NavigationItemResponse> children) {}
