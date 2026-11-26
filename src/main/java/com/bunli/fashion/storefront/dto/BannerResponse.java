package com.bunli.fashion.storefront.dto;

import java.time.LocalDateTime;

/**
 * Hero / promotional banner.
 * <p>
 * {@code imageUrl} duplicates {@code media.url} for one-line React usage:
 * {@code <img src={banner.imageUrl} />}.
 * </p>
 */
public record BannerResponse(
        Long          id,
        String        title,
        MediaResponse media,
        String        imageUrl,
        String        targetUrl,
        int           sortOrder,
        LocalDateTime startsAt,
        LocalDateTime endsAt
) {}
