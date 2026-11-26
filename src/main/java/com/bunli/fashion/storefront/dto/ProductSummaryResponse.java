package com.bunli.fashion.storefront.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record ProductSummaryResponse(Long id, String name, String slug, String description, BigDecimal basePrice,
                                     String gender, String material, boolean featured, MediaResponse image,
                                     LocalDateTime createdAt) {}
