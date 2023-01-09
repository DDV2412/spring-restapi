package com.ipmugo.library.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import com.ipmugo.library.data.Author;

@EnableJpaRepositories
public interface AuthorRepo extends JpaRepository<Author, UUID> {

    List<Author> findByArticleId(UUID article_id);

    Page<Author> findAll(Pageable pageable);

    @Query("SELECT a FROM Author a WHERE a.firstName = :firstName AND a.lastName = :lastName")
    Page<Author> findAllByPage(Pageable pageable, String firstName, String lastName);

    Author findByFirstNameAndLastNameAndArticleId(String firstName, String lastName, UUID article_id);
}
