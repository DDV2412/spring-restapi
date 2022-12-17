package com.ipmugo.library.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ipmugo.library.data.COAuthor;

public interface COAuthorRepo extends JpaRepository<COAuthor, UUID> {

}
