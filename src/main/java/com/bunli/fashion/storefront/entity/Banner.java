package com.bunli.fashion.storefront.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "banners")
public class Banner extends ActiveEntity {
    @Column(nullable = false) private String title;
    @Column(name = "media_id", nullable = false) private Long mediaId;
    @Column(name = "target_url") private String targetUrl;
    @Column(name = "sort_order") private int sortOrder;
    @Column(name = "starts_at") private LocalDateTime startsAt;
    @Column(name = "ends_at") private LocalDateTime endsAt;
    public String getTitle() { return title; }
    public Long getMediaId() { return mediaId; }
    public String getTargetUrl() { return targetUrl; }
    public int getSortOrder() { return sortOrder; }
    public LocalDateTime getStartsAt() { return startsAt; }
    public LocalDateTime getEndsAt() { return endsAt; }
}
