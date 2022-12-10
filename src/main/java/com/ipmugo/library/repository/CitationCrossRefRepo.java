package com.ipmugo.library.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ipmugo.library.data.CitationCrossRef;

public interface CitationCrossRefRepo extends JpaRepository<CitationCrossRef, UUID> {

    Optional<CitationCrossRef> findByArticleId(UUID id);

}
