package com.bunli.fashion.storefront.dto;

import java.util.List;

public record ProductSectionResponse(String key, String title, List<ProductSummaryResponse> products) {}
