package com.ipmugo.library.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ipmugo.library.data.Journal;

public interface JournalRepo extends JpaRepository<Journal, UUID> {

    Optional<Journal> findByIssn(String issn);

    Optional<Journal> findByAbbreviation(String abbreviation);
}
