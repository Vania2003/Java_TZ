package com.example.untitled.tvapp.repository;

import com.example.untitled.tvapp.model.Client;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClientRepository extends JpaRepository<Client, Long> {}