package com.bunli.fashion.storefront.repository;

import com.bunli.fashion.storefront.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ProductRepository extends JpaRepository<Product, Long> {
    Page<Product> findByActiveOrderByCreatedAtDesc(Byte active, Pageable pageable);
    Page<Product> findByActiveAndFeaturedOrderByCreatedAtDesc(Byte active, Byte featured, Pageable pageable);

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
