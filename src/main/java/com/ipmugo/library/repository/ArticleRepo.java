package com.ipmugo.library.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.ipmugo.library.data.Article;

public interface ArticleRepo extends JpaRepository<Article, UUID> {

    Optional<Article> findByDoi(String doi);

    Optional<Article> findByTitle(String title);

    Page<Article> findAll(Pageable pageable);
}
