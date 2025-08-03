package com.example.untitled.tvapp.repository;

import com.example.untitled.tvapp.model.PricingEntry;
import com.example.untitled.tvapp.model.ServiceType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.Optional;

public interface PricingEntryRepository extends JpaRepository<PricingEntry, Long> {
    @Query("SELECT p FROM PricingEntry p WHERE p.type = :type AND p.effectiveFrom <= :date AND (p.effectiveTo IS NULL OR p.effectiveTo >= :date) ORDER BY p.effectiveFrom DESC")
    Optional<PricingEntry> findCurrentByType(@Param("type") ServiceType type, @Param("date") LocalDate date);
}
