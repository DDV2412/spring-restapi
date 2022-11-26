package com.ipmugo.library.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ipmugo.library.data.LogSearch;

public interface LogSearchRepo extends JpaRepository<LogSearch, UUID> {

}
