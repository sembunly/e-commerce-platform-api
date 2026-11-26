package com.bunli.fashion.storefront.dto;

import java.util.List;

public record ArticlePageResponse(List<ArticleSummaryResponse> items, int page, int size, long totalElements,
                                  int totalPages) {}
