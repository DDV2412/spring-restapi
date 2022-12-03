package com.ipmugo.library.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ipmugo.library.data.File;

public interface FileRepo extends JpaRepository<File, UUID> {

    Optional<File> findByName(String setName);
}
