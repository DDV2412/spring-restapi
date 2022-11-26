package com.ipmugo.library.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ipmugo.library.data.Subject;

public interface SubjectRepo extends JpaRepository<Subject, UUID> {

    Optional<Subject> findByName(String name);
}
