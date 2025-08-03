package com.example.untitled.tvapp.repository;

import com.example.untitled.tvapp.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment, Long> {}