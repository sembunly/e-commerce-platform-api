package com.bunli.fashion.storefront.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "media")
public class Media extends ActiveEntity {
    @Column(name = "object_key", nullable = false) private String objectKey;
    @Column(nullable = false) private String bucket;
    @Column(name = "content_type") private String contentType;
    @Column(name = "size_bytes") private long sizeBytes;
    public String getObjectKey() { return objectKey; }
    public String getBucket() { return bucket; }
    public String getContentType() { return contentType; }
    public long getSizeBytes() { return sizeBytes; }
}
