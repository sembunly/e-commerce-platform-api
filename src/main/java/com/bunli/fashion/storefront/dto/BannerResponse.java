package com.bunli.fashion.storefront.dto;

import java.time.LocalDateTime;

public record BannerResponse(Long id, String title, MediaResponse media, String targetUrl, int sortOrder,
                             LocalDateTime startsAt, LocalDateTime endsAt) {}
