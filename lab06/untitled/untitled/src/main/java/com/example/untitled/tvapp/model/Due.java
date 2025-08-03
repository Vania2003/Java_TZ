package com.example.untitled.tvapp.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
public class Due {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDate dueDate;
    private double amount;
    private boolean paid;

    @ManyToOne(fetch = FetchType.LAZY)
    private Subscription subscription;

    public Due() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public LocalDate getDueDate() { return dueDate; }
    public void setDueDate(LocalDate dueDate) { this.dueDate = dueDate; }

    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }

    public boolean isPaid() { return paid; }
    public void setPaid(boolean paid) { this.paid = paid; }

    public Subscription getSubscription() { return subscription; }
    public void setSubscription(Subscription subscription) { this.subscription = subscription; }

    @Override
    public String toString() {
        return "Due{id=" + id + ", dueDate=" + dueDate + ", amount=" + amount + ", paid=" + paid + "}";
    }
}