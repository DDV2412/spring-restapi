package com.ipmugo.library.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ipmugo.library.data.Metric;

public interface MetricRepo extends JpaRepository<Metric, UUID> {

    Optional<Metric> findByJournalId(UUID id);
}
