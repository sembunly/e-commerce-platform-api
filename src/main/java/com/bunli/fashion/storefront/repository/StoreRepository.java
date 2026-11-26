package com.bunli.fashion.storefront.repository;

import com.bunli.fashion.storefront.entity.Store;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StoreRepository extends JpaRepository<Store, Long> {
    List<Store> findByActiveOrderByNameAsc(Byte active);
    Optional<Store> findByIdAndActive(Long id, Byte active);
}
