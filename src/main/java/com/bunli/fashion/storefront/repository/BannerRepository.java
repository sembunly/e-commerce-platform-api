package com.bunli.fashion.storefront.repository;

import com.bunli.fashion.storefront.entity.Banner;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface BannerRepository extends JpaRepository<Banner, Long> {
    @Query("""
            select b from Banner b where b.active = :active
            and (b.startsAt is null or b.startsAt <= :now)
            and (b.endsAt is null or b.endsAt >= :now)
            order by b.sortOrder asc, b.id asc
            """)
    List<Banner> findVisible(@Param("active") Byte active, @Param("now") LocalDateTime now);
}
