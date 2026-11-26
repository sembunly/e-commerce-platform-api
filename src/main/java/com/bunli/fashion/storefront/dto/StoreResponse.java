package com.bunli.fashion.storefront.dto;

import java.math.BigDecimal;

public record StoreResponse(Long id, String name, String address, String phone, String openingHours,
                            BigDecimal latitude, BigDecimal longitude) {}
