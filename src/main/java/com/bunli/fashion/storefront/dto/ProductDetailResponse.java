package com.bunli.fashion.storefront.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record ProductDetailResponse(Long id, String name, String slug, String description, BigDecimal basePrice,
                                    String gender, String material, boolean featured, List<CategoryResponse> categories,
                                    List<CollectionResponse> collections, List<ProductImageResponse> images,
                                    List<VariantResponse> variants, LocalDateTime createdAt) {}
