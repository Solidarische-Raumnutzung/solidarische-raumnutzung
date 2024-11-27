package edu.kit.hci.soli.repository;

import org.springframework.stereotype.Service;

import java.util.concurrent.atomic.LongAdder;

@Service
public class VisitsRepository  {
    private final LongAdder visits = new LongAdder();

    public void increment() {
        visits.increment();
    }

    public long getVisits() {
        return visits.sum();
    }
}
