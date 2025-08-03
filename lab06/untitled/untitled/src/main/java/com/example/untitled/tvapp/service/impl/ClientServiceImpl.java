package com.example.untitled.tvapp.service.impl;

import com.example.untitled.tvapp.model.Client;
import com.example.untitled.tvapp.repository.ClientRepository;
import com.example.untitled.tvapp.service.ClientService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@Transactional
public class ClientServiceImpl implements ClientService {
    private final ClientRepository repo;
    public ClientServiceImpl(ClientRepository repo) { this.repo = repo; }
    @Override public Client createClient(String f, String l) {
        var c = new Client(); c.setFirstName(f); c.setLastName(l);
        return repo.save(c);
    }
    @Override public List<Client> listAll() { return repo.findAll(); }
}