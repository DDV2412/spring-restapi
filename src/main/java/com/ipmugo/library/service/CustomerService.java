package com.ipmugo.library.service;

import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.ipmugo.library.data.Customer;
import com.ipmugo.library.repository.CustomerRepo;

import jakarta.transaction.TransactionScoped;

@Service
@TransactionScoped
public class CustomerService {

    @Autowired
    private CustomerRepo customerRepo;

    public Page<Customer> findAll(Pageable pageable) {
        return customerRepo.findAll(pageable);
    }

    public Customer findOne(UUID id) {
        Optional<Customer> customer = customerRepo.findById(id);

        if (!customer.isPresent()) {
            return null;
        }

        return customer.get();
    }

    public Customer findByEmail(String email) {
        Optional<Customer> customer = customerRepo.findByEmail(email);

        if (!customer.isPresent()) {
            return null;
        }

        return customer.get();
    }

    public Customer save(Customer customer) {
        return customerRepo.save(customer);
    }

    public void deleteById(UUID id) {
        customerRepo.deleteById(id);
    }
}
