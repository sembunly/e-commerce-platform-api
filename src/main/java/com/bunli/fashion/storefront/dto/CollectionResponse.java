package com.bunli.fashion.storefront.dto;

/**
 * Featured collection card — displayed in the "Collections" mega-menu tab
 * and the homepage featured-collections row.
 */
public record CollectionResponse(
        Long   id,
        String name,
        String slug,
        String description,
        MediaResponse image,
        /** Convenience top-level URL duplicated from image.url */
        String imageUrl
) {}
