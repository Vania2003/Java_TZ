package com.example.untitled.tvapp.service;

import com.example.untitled.tvapp.model.PricingEntry;
import com.example.untitled.tvapp.model.ServiceType;

import java.time.LocalDate;
import java.util.List;

public interface PricingService {
    PricingEntry addPricingEntry(ServiceType type, double price, LocalDate effectiveFrom, LocalDate effectiveTo);
    List<PricingEntry> listPricingEntries();
}