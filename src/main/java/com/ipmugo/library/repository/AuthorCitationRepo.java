package com.ipmugo.library.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ipmugo.library.data.AuthorCitation;

public interface AuthorCitationRepo extends JpaRepository<AuthorCitation, UUID> {

}
