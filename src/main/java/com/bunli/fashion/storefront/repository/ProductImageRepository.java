package com.bunli.fashion.storefront.repository;

import com.bunli.fashion.storefront.entity.ProductImage;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductImageRepository extends JpaRepository<ProductImage, Long> {
    Optional<ProductImage> findFirstByProductIdAndActiveOrderBySortOrderAscIdAsc(Long productId, Byte active);
    List<ProductImage> findByProductIdInAndActiveOrderByProductIdAscSortOrderAscIdAsc(Collection<Long> productIds, Byte active);
}
