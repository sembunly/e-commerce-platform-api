package com.bunli.fashion.storefront.repository;

import com.bunli.fashion.storefront.entity.Collection;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CollectionRepository extends JpaRepository<Collection, Long> {
    List<Collection> findByActiveOrderByNameAsc(Byte active);
    Optional<Collection> findBySlugAndActive(String slug, Byte active);

    @org.springframework.data.jpa.repository.Query(value = """
            select c.* from collections c join product_collections pc on pc.collection_id = c.id
            where pc.product_id = :productId and pc.is_active = 1 and c.is_active = 1 order by c.name asc
            """, nativeQuery = true)
    List<Collection> findActiveByProductId(@org.springframework.data.repository.query.Param("productId") Long productId);

}
