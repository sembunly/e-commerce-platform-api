package com.bunli.fashion.storefront.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "articles")
public class Article extends ActiveEntity {
    @Column(nullable = false) private String name;
    @Column(nullable = false) private String slug;
    @Column(nullable = false) private String content;
    @Column(name = "media_id") private Long mediaId;
    @Column(name = "author_id") private Long authorId;
    @Column(name = "published_at") private LocalDateTime publishedAt;
    public String getName() { return name; }
    public String getSlug() { return slug; }
    public String getContent() { return content; }
    public Long getMediaId() { return mediaId; }
    public Long getAuthorId() { return authorId; }
    public LocalDateTime getPublishedAt() { return publishedAt; }
}
