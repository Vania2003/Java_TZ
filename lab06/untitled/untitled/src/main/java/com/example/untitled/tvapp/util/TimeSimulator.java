package com.example.untitled.tvapp.util;

import org.springframework.stereotype.Component;
import java.time.LocalDate;

@Component
public class TimeSimulator {
    private LocalDate now = LocalDate.now();
    public LocalDate today() { return now; }
    public void advanceDay() { now = now.plusDays(1); }
    public void advanceMonth() { now = now.plusMonths(1); }
}