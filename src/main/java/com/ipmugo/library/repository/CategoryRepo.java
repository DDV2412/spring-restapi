package com.ipmugo.library.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ipmugo.library.data.Category;

public interface CategoryRepo extends JpaRepository<Category, UUID> {

    Optional<Category> findByName(String name);

}
