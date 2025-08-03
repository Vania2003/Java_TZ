package com.example.untitled.tvapp.service;

import com.example.untitled.tvapp.model.Due;
import com.example.untitled.tvapp.model.Payment;
import java.time.LocalDate;
import java.util.List;

public interface AccountingService {
    void generateMonthlyDues();
    List<Due> listDues();
    Payment recordPayment(Long dueId, LocalDate paymentDate, double amount);
}