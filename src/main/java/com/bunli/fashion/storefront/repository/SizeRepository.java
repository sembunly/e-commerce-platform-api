package com.bunli.fashion.storefront.repository;

import com.bunli.fashion.storefront.entity.Size;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SizeRepository extends JpaRepository<Size, Long> {
    List<Size> findByActiveOrderBySortOrderAscNameAsc(Byte active);
}
