package com.bunli.fashion.storefront.repository;

import com.bunli.fashion.storefront.entity.Color;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ColorRepository extends JpaRepository<Color, Long> {
    List<Color> findByActiveOrderByNameAsc(Byte active);
}
