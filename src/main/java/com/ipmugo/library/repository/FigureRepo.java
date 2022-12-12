package com.ipmugo.library.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ipmugo.library.data.Figure;

public interface FigureRepo extends JpaRepository<Figure, UUID> {

    Optional<Figure> findByFilename(String filename);
}
