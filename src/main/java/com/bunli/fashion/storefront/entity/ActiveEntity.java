package com.bunli.fashion.storefront.entity;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;

@MappedSuperclass
public abstract class ActiveEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;

    @Column(name = "is_active", nullable = false)
    protected Byte active;

    public Long getId() { return id; }
    public boolean isActive() { return active != null && active == 1; }
}
