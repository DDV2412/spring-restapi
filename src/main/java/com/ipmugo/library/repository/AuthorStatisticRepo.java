package com.ipmugo.library.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ipmugo.library.data.AuthorStatistic;

public interface AuthorStatisticRepo extends JpaRepository<AuthorStatistic, UUID> {

}
