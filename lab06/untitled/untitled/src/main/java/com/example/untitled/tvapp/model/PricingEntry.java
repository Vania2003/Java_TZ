package com.example.untitled.tvapp.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
public class PricingEntry {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private ServiceType type;
    private double price;
    private LocalDate effectiveFrom;
    private LocalDate effectiveTo;

    public PricingEntry() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public ServiceType getType() { return type; }
    public void setType(ServiceType type) { this.type = type; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    public LocalDate getEffectiveFrom() { return effectiveFrom; }
    public void setEffectiveFrom(LocalDate effectiveFrom) { this.effectiveFrom = effectiveFrom; }

    public LocalDate getEffectiveTo() { return effectiveTo; }
    public void setEffectiveTo(LocalDate effectiveTo) { this.effectiveTo = effectiveTo; }

    @Override
    public String toString() {
        return "PricingEntry{id=" + id + ", type=" + type + ", price=" + price + ", from=" + effectiveFrom + ", to=" + effectiveTo + "}";
    }
}