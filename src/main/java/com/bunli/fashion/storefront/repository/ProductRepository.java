package com.bunli.fashion.storefront.repository;

import com.bunli.fashion.storefront.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.math.BigDecimal;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {
     Page<Product> findByActiveOrderByCreatedAtDesc(Byte active, Pageable pageable);

     Page<Product> findByActiveAndFeaturedOrderByCreatedAtDesc(Byte active, Byte featured, Pageable pageable);

     /** New arrivals filtered by gender (e.g. "WOMEN", "MEN"). */
     Page<Product> findByActiveAndGenderIgnoreCaseOrderByCreatedAtDesc(Byte active, String gender, Pageable pageable);

     @Query(value = """
               select p.* from products p
               where p.is_active = 1
                 and (:keyword is null or lower(p.name) like lower(concat('%', :keyword, '%')))
                 and (:gender is null or lower(p.gender) = lower(:gender))
                 and (:categorySlug is null or exists (select 1 from product_categories pc join categories c on c.id = pc.category_id
                      where pc.product_id = p.id and pc.is_active = 1 and c.is_active = 1 and c.slug = :categorySlug))
                 and (:collectionSlug is null or exists (select 1 from product_collections pc join collections c on c.id = pc.collection_id
                      where pc.product_id = p.id and pc.is_active = 1 and c.is_active = 1 and c.slug = :collectionSlug))
                 and (:sizeName is null or exists (select 1 from product_variants pv join sizes s on s.id = pv.size_id
                      where pv.product_id = p.id and pv.is_active = 1 and s.is_active = 1 and lower(s.name) = lower(:sizeName)))
                 and (:colorName is null or exists (select 1 from product_variants pv join colors c on c.id = pv.color_id
                      where pv.product_id = p.id and pv.is_active = 1 and c.is_active = 1 and lower(c.name) = lower(:colorName)))
                 and (:minPrice is null or exists (select 1 from product_variants pv where pv.product_id = p.id and pv.is_active = 1
                      and coalesce(pv.sale_price, pv.price) >= :minPrice))
                 and (:maxPrice is null or exists (select 1 from product_variants pv where pv.product_id = p.id and pv.is_active = 1
                      and coalesce(pv.sale_price, pv.price) <= :maxPrice))
                 and (:inStock is null or :inStock = false or exists (select 1 from product_variants pv where pv.product_id = p.id
                      and pv.is_active = 1 and pv.stock_quantity > pv.reserved_quantity))
               order by
                 case when :sort = 'price_asc' then coalesce((select min(coalesce(pv.sale_price, pv.price)) from product_variants pv where pv.product_id = p.id and pv.is_active = 1), p.base_price) end asc,
                 case when :sort = 'price_desc' then coalesce((select min(coalesce(pv.sale_price, pv.price)) from product_variants pv where pv.product_id = p.id and pv.is_active = 1), p.base_price) end desc,
                 case when :sort = 'name_asc' then p.name end asc,
                 case when :sort = 'name_desc' then p.name end desc,
                 p.created_at desc
               """, countQuery = """
               select count(*) from products p where p.is_active = 1
                 and (:keyword is null or lower(p.name) like lower(concat('%', :keyword, '%')))
                 and (:gender is null or lower(p.gender) = lower(:gender))
                 and (:categorySlug is null or exists (select 1 from product_categories pc join categories c on c.id = pc.category_id where pc.product_id = p.id and pc.is_active = 1 and c.is_active = 1 and c.slug = :categorySlug))
                 and (:collectionSlug is null or exists (select 1 from product_collections pc join collections c on c.id = pc.collection_id where pc.product_id = p.id and pc.is_active = 1 and c.is_active = 1 and c.slug = :collectionSlug))
                 and (:sizeName is null or exists (select 1 from product_variants pv join sizes s on s.id = pv.size_id where pv.product_id = p.id and pv.is_active = 1 and s.is_active = 1 and lower(s.name) = lower(:sizeName)))
                 and (:colorName is null or exists (select 1 from product_variants pv join colors c on c.id = pv.color_id where pv.product_id = p.id and pv.is_active = 1 and c.is_active = 1 and lower(c.name) = lower(:colorName)))
                 and (:minPrice is null or exists (select 1 from product_variants pv where pv.product_id = p.id and pv.is_active = 1 and coalesce(pv.sale_price, pv.price) >= :minPrice))
                 and (:maxPrice is null or exists (select 1 from product_variants pv where pv.product_id = p.id and pv.is_active = 1 and coalesce(pv.sale_price, pv.price) <= :maxPrice))
                 and (:inStock is null or :inStock = false or exists (select 1 from product_variants pv where pv.product_id = p.id and pv.is_active = 1 and pv.stock_quantity > pv.reserved_quantity))
               """, nativeQuery = true)
     Page<Product> findCatalog(@Param("keyword") String keyword, @Param("gender") String gender,
               @Param("categorySlug") String categorySlug, @Param("collectionSlug") String collectionSlug,
               @Param("sizeName") String sizeName, @Param("colorName") String colorName,
               @Param("minPrice") BigDecimal minPrice, @Param("maxPrice") BigDecimal maxPrice,
               @Param("inStock") Boolean inStock, @Param("sort") String sort, Pageable pageable);

     Optional<Product> findBySlugAndActive(String slug, Byte active);

     @Query(value = """
               select p.* from products p where p.is_active = 1 and p.id <> :productId and
               (exists (select 1 from product_categories mine join product_categories other on mine.category_id = other.category_id
                where mine.product_id = :productId and mine.is_active = 1 and other.product_id = p.id and other.is_active = 1)
                or exists (select 1 from product_collections mine join product_collections other on mine.collection_id = other.collection_id
                where mine.product_id = :productId and mine.is_active = 1 and other.product_id = p.id and other.is_active = 1))
               order by p.created_at desc
               """, countQuery = """
               select count(*) from products p where p.is_active = 1 and p.id <> :productId and
               (exists (select 1 from product_categories mine join product_categories other on mine.category_id = other.category_id where mine.product_id = :productId and mine.is_active = 1 and other.product_id = p.id and other.is_active = 1)
                or exists (select 1 from product_collections mine join product_collections other on mine.collection_id = other.collection_id where mine.product_id = :productId and mine.is_active = 1 and other.product_id = p.id and other.is_active = 1))
               """, nativeQuery = true)
     Page<Product> findRelated(@Param("productId") Long productId, Pageable pageable);

     @Query(value = "select min(coalesce(sale_price, price)) from product_variants where is_active = 1", nativeQuery = true)
     Optional<BigDecimal> findCatalogMinPrice();

     @Query(value = "select max(coalesce(sale_price, price)) from product_variants where is_active = 1", nativeQuery = true)
     Optional<BigDecimal> findCatalogMaxPrice();

     @Query(value = """
               select p.* from products p join product_variants pv on pv.product_id = p.id
               join order_items oi on oi.variant_id = pv.id join orders o on o.id = oi.order_id
               where p.is_active = 1 and pv.is_active = 1 and o.status <> 'CANCELLED'
               group by p.id order by sum(oi.quantity) desc, p.created_at desc
               """, countQuery = """
               select count(distinct p.id) from products p join product_variants pv on pv.product_id = p.id
               join order_items oi on oi.variant_id = pv.id join orders o on o.id = oi.order_id
               where p.is_active = 1 and pv.is_active = 1 and o.status <> 'CANCELLED'
               """, nativeQuery = true)
     Page<Product> findBestSellers(Pageable pageable);

     @Query(value = """
               select distinct p.* from products p join product_variants pv on pv.product_id = p.id
               where p.is_active = 1 and pv.is_active = 1 and pv.sale_price is not null and pv.sale_price < pv.price
               order by p.created_at desc
               """, countQuery = """
               select count(distinct p.id) from products p join product_variants pv on pv.product_id = p.id
               where p.is_active = 1 and pv.is_active = 1 and pv.sale_price is not null and pv.sale_price < pv.price
               """, nativeQuery = true)
     Page<Product> findSaleProducts(Pageable pageable);
}
