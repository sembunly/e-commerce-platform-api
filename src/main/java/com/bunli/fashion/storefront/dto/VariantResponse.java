package com.bunli.fashion.storefront.dto;

import java.math.BigDecimal;

public record VariantResponse(Long id, String sku, BigDecimal price, BigDecimal salePrice, int stockQuantity,
                              int reservedQuantity, int availableQuantity, Long sizeId, String sizeName,
                              Long colorId, String colorName, String colorHexCode) {}
