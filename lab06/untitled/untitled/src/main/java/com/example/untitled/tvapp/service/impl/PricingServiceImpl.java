package com.example.untitled.tvapp.service.impl;

import com.example.untitled.tvapp.model.PricingEntry;
import com.example.untitled.tvapp.model.ServiceType;
import com.example.untitled.tvapp.repository.PricingEntryRepository;
import com.example.untitled.tvapp.service.PricingService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@Transactional
public class PricingServiceImpl implements PricingService {
    private final PricingEntryRepository pricingRepo;

    public PricingServiceImpl(PricingEntryRepository pricingRepo) {
        this.pricingRepo = pricingRepo;
    }

    @Override
    public PricingEntry addPricingEntry(ServiceType type, double price, LocalDate effectiveFrom, LocalDate effectiveTo) {
        PricingEntry entry = new PricingEntry();
        entry.setType(type);
        entry.setPrice(price);
        entry.setEffectiveFrom(effectiveFrom);
        entry.setEffectiveTo(effectiveTo);
        return pricingRepo.save(entry);
    }

    @Override
    public List<PricingEntry> listPricingEntries() {
        return pricingRepo.findAll();
    }
}