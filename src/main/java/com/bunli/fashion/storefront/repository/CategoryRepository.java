package com.bunli.fashion.storefront.repository;

import com.bunli.fashion.storefront.entity.Category;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    List<Category> findByActiveOrderBySortOrderAscNameAsc(Byte active);
    List<Category> findByParentIdAndActiveOrderBySortOrderAscNameAsc(Long parentId, Byte active);
    Optional<Category> findBySlugAndActive(String slug, Byte active);

    @org.springframework.data.jpa.repository.Query(value = """
            select c.* from categories c join product_categories pc on pc.category_id = c.id
            where pc.product_id = :productId and pc.is_active = 1 and c.is_active = 1
            order by c.sort_order asc, c.name asc
            """, nativeQuery = true)
    List<Category> findActiveByProductId(@org.springframework.data.repository.query.Param("productId") Long productId);
}
