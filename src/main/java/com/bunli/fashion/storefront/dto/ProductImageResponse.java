package com.bunli.fashion.storefront.dto;

public record ProductImageResponse(Long id, MediaResponse media, Long colorId, String altText, int sortOrder) {}
