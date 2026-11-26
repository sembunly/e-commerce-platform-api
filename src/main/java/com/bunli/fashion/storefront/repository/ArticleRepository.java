package com.bunli.fashion.storefront.repository;

import com.bunli.fashion.storefront.entity.Article;
import java.time.LocalDateTime;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArticleRepository extends JpaRepository<Article, Long> {
    Page<Article> findByActiveAndPublishedAtLessThanEqualOrderByPublishedAtDesc(Byte active, LocalDateTime now, Pageable pageable);
    Optional<Article> findBySlugAndActiveAndPublishedAtLessThanEqual(String slug, Byte active, LocalDateTime now);
}
