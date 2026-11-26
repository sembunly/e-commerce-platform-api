package com.bunli.fashion.storefront.dto;

import java.util.List;

public record ProductPageResponse(List<ProductSummaryResponse> items, int page, int size, long totalElements,
                                  int totalPages) {}
