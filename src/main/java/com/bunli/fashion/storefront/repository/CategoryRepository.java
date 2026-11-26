package com.bunli.fashion.storefront.repository;

import com.bunli.fashion.storefront.entity.Category;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    List<Category> findByActiveOrderBySortOrderAscNameAsc(Byte active);
    List<Category> findByParentIdAndActiveOrderBySortOrderAscNameAsc(Long parentId, Byte active);
    Optional<Category> findBySlugAndActive(String slug, Byte active);
}
