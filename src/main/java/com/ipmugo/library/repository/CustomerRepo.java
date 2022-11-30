package com.ipmugo.library.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ipmugo.library.data.Customer;

public interface CustomerRepo extends JpaRepository<Customer, UUID> {

    Optional<Customer> findByEmail(String email);
}
