package com.example.untitled.tvapp.model;

import jakarta.persistence.*;
import java.time.LocalDate;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
public class Payment {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDate paymentDate;
    private double amount;

    @ManyToOne(fetch = FetchType.LAZY)
    private Subscription subscription;

    public Payment() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public LocalDate getPaymentDate() { return paymentDate; }
    public void setPaymentDate(LocalDate paymentDate) { this.paymentDate = paymentDate; }

    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }

    public Subscription getSubscription() { return subscription; }
    public void setSubscription(Subscription subscription) { this.subscription = subscription; }

    @Override
    public String toString() {
        return "Payment{id=" + id + ", paymentDate=" + paymentDate + ", amount=" + amount + "}";
    }
}