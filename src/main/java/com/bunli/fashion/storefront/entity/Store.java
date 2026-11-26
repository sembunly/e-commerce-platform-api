package com.bunli.fashion.storefront.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "stores")
public class Store extends ActiveEntity {
    @Column(nullable = false) private String name;
    @Column(nullable = false) private String address;
    private String phone;
    @Column(name = "opening_hours") private String openingHours;
    private BigDecimal latitude;
    private BigDecimal longitude;
    public String getName() { return name; }
    public String getAddress() { return address; }
    public String getPhone() { return phone; }
    public String getOpeningHours() { return openingHours; }
    public BigDecimal getLatitude() { return latitude; }
    public BigDecimal getLongitude() { return longitude; }
}
