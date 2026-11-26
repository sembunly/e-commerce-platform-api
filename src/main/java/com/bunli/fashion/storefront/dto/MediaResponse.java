package com.bunli.fashion.storefront.dto;

/**
 * Lightweight media descriptor returned with every image-carrying response.
 * <p>
 * {@code url} is a pre-built convenience field for React — compose it from
 * the MinIO/CDN base URL + {@code objectKey} so the front-end can render
 * {@code <img src={media.url} />} without any extra logic.
 * </p>
 */
public record MediaResponse(
                Long id,
                String bucket,
                String objectKey,
                String contentType,
                long sizeBytes,
                String url) {
}
