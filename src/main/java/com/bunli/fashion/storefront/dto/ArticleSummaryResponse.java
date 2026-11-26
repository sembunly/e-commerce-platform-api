package com.bunli.fashion.storefront.dto;

import java.time.LocalDateTime;

public record ArticleSummaryResponse(Long id, String name, String slug, MediaResponse image, LocalDateTime publishedAt) {}
