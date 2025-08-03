package com.example.untitled.tvapp.model;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Subscription {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Client client;

    @Enumerated(EnumType.STRING)
    private ServiceType type;

    @OneToMany(mappedBy = "subscription", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Subaccount> subaccounts = new ArrayList<>();

    @OneToMany(mappedBy = "subscription", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Due> dues = new ArrayList<>();

    @OneToMany(mappedBy = "subscription", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Payment> payments = new ArrayList<>();

    public Subscription() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Client getClient() { return client; }
    public void setClient(Client client) { this.client = client; }

    public ServiceType getType() { return type; }
    public void setType(ServiceType type) { this.type = type; }

    public List<Subaccount> getSubaccounts() { return subaccounts; }
    public void setSubaccounts(List<Subaccount> subaccounts) { this.subaccounts = subaccounts; }

    public List<Due> getDues() { return dues; }
    public void setDues(List<Due> dues) { this.dues = dues; }

    public List<Payment> getPayments() { return payments; }
    public void setPayments(List<Payment> payments) { this.payments = payments; }

    @Override
    public String toString() {
        return "Subscription{id=" + id + ", type=" + type + ", clientId=" + (client != null ? client.getId() : null) + "}";
    }
}