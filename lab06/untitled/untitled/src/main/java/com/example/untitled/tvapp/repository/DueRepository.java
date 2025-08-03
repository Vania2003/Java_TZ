package com.example.untitled.tvapp.repository;

import com.example.untitled.tvapp.model.Due;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface DueRepository extends JpaRepository<Due, Long> {
    List<Due> findByPaidFalse();
}