package com.example.untitled.tvapp.service.impl;

import com.example.untitled.tvapp.model.Client;
import com.example.untitled.tvapp.model.ServiceType;
import com.example.untitled.tvapp.model.Subscription;
import com.example.untitled.tvapp.model.Subaccount;
import com.example.untitled.tvapp.repository.ClientRepository;
import com.example.untitled.tvapp.repository.SubscriptionRepository;
import com.example.untitled.tvapp.repository.SubaccountRepository;
import com.example.untitled.tvapp.service.SubscriptionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class SubscriptionServiceImpl implements SubscriptionService {
    private final SubscriptionRepository subRepo;
    private final ClientRepository clientRepo;
    private final SubaccountRepository subAccRepo;

    public SubscriptionServiceImpl(SubscriptionRepository subRepo,
                                   ClientRepository clientRepo,
                                   SubaccountRepository subAccRepo) {
        this.subRepo = subRepo;
        this.clientRepo = clientRepo;
        this.subAccRepo = subAccRepo;
    }

    @Override
    public Subscription createSubscription(Long clientId, ServiceType type) {
        Client client = clientRepo.findById(clientId)
                .orElseThrow(() -> new IllegalArgumentException("Client not found: " + clientId));
        Subscription sub = new Subscription();
        sub.setClient(client);
        sub.setType(type);
        return subRepo.save(sub);
    }

    @Override
    public List<Subscription> listSubscriptions(Long clientId) {
        return subRepo.findByClientId(clientId);
    }

    @Override
    public void addSubaccount(Long subscriptionId, String login, String password) {
        Subscription sub = subRepo.findById(subscriptionId)
                .orElseThrow(() -> new IllegalArgumentException("Subscription not found: " + subscriptionId));
        Subaccount sa = new Subaccount();
        sa.setLogin(login);
        sa.setPassword(password);
        sa.setSubscription(sub);
        subAccRepo.save(sa);
    }
}