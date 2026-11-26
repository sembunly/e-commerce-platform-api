package com.bunli.fashion.storefront.dto;

import java.util.List;

public record CategoryResponse(Long id, Long parentId, String name, String slug, String description,
                               MediaResponse image, int sortOrder, List<CategoryResponse> children) {}
