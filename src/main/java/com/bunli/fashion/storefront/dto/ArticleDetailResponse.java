package com.bunli.fashion.storefront.dto;

import java.time.LocalDateTime;

public record ArticleDetailResponse(Long id, String name, String slug, String content, MediaResponse image,
                                    Long authorId, LocalDateTime publishedAt) {}
