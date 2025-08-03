package com.example.untitled.tvapp.model;

import jakarta.persistence.*;

import jakarta.persistence.*;

@Entity
public class Subaccount {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String login;
    private String password;

    @ManyToOne(fetch = FetchType.LAZY)
    private Subscription subscription;

    public Subaccount() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getLogin() { return login; }
    public void setLogin(String login) { this.login = login; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public Subscription getSubscription() { return subscription; }
    public void setSubscription(Subscription subscription) { this.subscription = subscription; }

    @Override
    public String toString() {
        return "Subaccount{id=" + id + ", login='" + login + "'" + "}";
    }
}