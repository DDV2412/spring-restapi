package com.ipmugo.library.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ipmugo.library.data.CitationScopus;

public interface CitationScopusRepo extends JpaRepository<CitationScopus, UUID> {

    Optional<CitationScopus> findByArticleId(UUID id);

}
