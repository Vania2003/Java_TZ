package com.example.untitled.tvapp.service;

import com.example.untitled.tvapp.model.Client;
import java.util.List;

public interface ClientService {
    Client createClient(String firstName, String lastName);
    List<Client> listAll();
}