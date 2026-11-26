package com.bunli.fashion.storefront.dto;

public record MediaResponse(Long id, String bucket, String objectKey, String contentType, long sizeBytes) {}
