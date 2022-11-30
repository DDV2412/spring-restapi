package com.ipmugo.library.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ipmugo.library.data.Author;

public interface AuthorRepo extends JpaRepository<Author, UUID> {

    List<Author> findByArticleId(UUID article_id);
}