package com.bunli.fashion.storefront.repository;

import com.bunli.fashion.storefront.entity.Media;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MediaRepository extends JpaRepository<Media, Long> {
    Optional<Media> findByIdAndActive(Long id, Byte active);
    List<Media> findByIdInAndActive(Collection<Long> ids, Byte active);
}
