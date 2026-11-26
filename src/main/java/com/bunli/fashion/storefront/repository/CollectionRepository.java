package com.bunli.fashion.storefront.repository;

import com.bunli.fashion.storefront.entity.Collection;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CollectionRepository extends JpaRepository<Collection, Long> {
    List<Collection> findByActiveOrderByNameAsc(Byte active);
    Optional<Collection> findBySlugAndActive(String slug, Byte active);
}
