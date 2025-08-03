package com.example.untitled.tvapp.service;

import com.example.untitled.tvapp.model.ServiceType;
import com.example.untitled.tvapp.model.Subscription;

import java.util.List;

public interface SubscriptionService {
    Subscription createSubscription(Long clientId, ServiceType type);
    List<Subscription> listSubscriptions(Long clientId);
    void addSubaccount(Long subscriptionId, String login, String password);
}