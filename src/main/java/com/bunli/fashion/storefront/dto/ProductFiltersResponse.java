package com.bunli.fashion.storefront.dto;

import java.math.BigDecimal;
import java.util.List;

public record ProductFiltersResponse(List<CategoryResponse> categories, List<CollectionResponse> collections,
                                     List<FilterOptionResponse> sizes, List<FilterOptionResponse> colors,
                                     BigDecimal minPrice, BigDecimal maxPrice) {}
