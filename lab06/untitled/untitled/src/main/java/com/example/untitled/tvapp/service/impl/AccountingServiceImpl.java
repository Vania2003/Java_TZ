package com.example.untitled.tvapp.service.impl;

import com.example.untitled.tvapp.model.Due;
import com.example.untitled.tvapp.model.Payment;
import com.example.untitled.tvapp.model.PricingEntry;
import com.example.untitled.tvapp.model.Subscription;
import com.example.untitled.tvapp.repository.DueRepository;
import com.example.untitled.tvapp.repository.PaymentRepository;
import com.example.untitled.tvapp.repository.PricingEntryRepository;
import com.example.untitled.tvapp.repository.SubscriptionRepository;
import com.example.untitled.tvapp.service.AccountingService;
import com.example.untitled.tvapp.util.TimeSimulator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@Transactional
public class AccountingServiceImpl implements AccountingService {

    private final SubscriptionRepository subscriptionRepo;
    private final PricingEntryRepository pricingRepo;
    private final DueRepository dueRepo;
    private final PaymentRepository paymentRepo;
    private final TimeSimulator timeSimulator;

    public AccountingServiceImpl(SubscriptionRepository subscriptionRepo,
                                 PricingEntryRepository pricingRepo,
                                 DueRepository dueRepo,
                                 PaymentRepository paymentRepo,
                                 TimeSimulator timeSimulator) {
        this.subscriptionRepo = subscriptionRepo;
        this.pricingRepo = pricingRepo;
        this.dueRepo = dueRepo;
        this.paymentRepo = paymentRepo;
        this.timeSimulator = timeSimulator;
    }

    @Override
    public void generateMonthlyDues() {
        LocalDate today = timeSimulator.today();
        List<Subscription> subs = subscriptionRepo.findAll();
        for (Subscription sub : subs) {
            PricingEntry entry = pricingRepo
                    .findCurrentByType(sub.getType(), today)
                    .orElseThrow(() -> new IllegalStateException("No pricing for type " + sub.getType()));
            Due due = new Due();
            due.setSubscription(sub);
            due.setDueDate(today.plusMonths(1));
            due.setAmount(entry.getPrice());
            due.setPaid(false);
            dueRepo.save(due);
        }
    }

    @Override
    public List<Due> listDues() {
        return dueRepo.findAll();
    }

    @Override
    public Payment recordPayment(Long dueId, LocalDate paymentDate, double amount) {
        Due due = dueRepo.findById(dueId)
                .orElseThrow(() -> new IllegalArgumentException("Due not found: " + dueId));
        due.setPaid(true);
        dueRepo.save(due);

        Payment payment = new Payment();
        payment.setSubscription(due.getSubscription());
        payment.setPaymentDate(paymentDate);
        payment.setAmount(amount);
        return paymentRepo.save(payment);
    }
}