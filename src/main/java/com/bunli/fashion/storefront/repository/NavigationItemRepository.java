package com.bunli.fashion.storefront.repository;

import com.bunli.fashion.storefront.entity.NavigationItem;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NavigationItemRepository extends JpaRepository<NavigationItem, Long> {
    List<NavigationItem> findByActiveOrderBySortOrderAscNameAsc(Byte active);
    Optional<NavigationItem> findBySlugAndActive(String slug, Byte active);
}
