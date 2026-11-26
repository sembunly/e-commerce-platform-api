package com.bunli.fashion.storefront.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "colors")
public class Color extends ActiveEntity {
    @Column(nullable = false)
    private String name;
    @Column(name = "hex_code", nullable = false)
    private String hexCode;

    public String getName() {
        return name;
    }

    public String getHexCode() {
        return hexCode;
    }
}
