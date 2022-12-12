package com.ipmugo.library.controllers;

import java.util.UUID;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ipmugo.library.data.Customer;
import com.ipmugo.library.dto.ResponseData;
import com.ipmugo.library.service.CustomerService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/customer")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @Autowired
    private ModelMapper modelMapper;

    @GetMapping
    public ResponseEntity<ResponseData<Iterable<Customer>>> findAll(@RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "15") int size) {
        ResponseData<Iterable<Customer>> responseData = new ResponseData<>();

        Pageable pageable = PageRequest.of(page, size);

        Page<Customer> customer = customerService.findAll(pageable);

        responseData.setStatus(true);
        responseData.setPayload(customer.getContent());
        return ResponseEntity.ok(responseData);

    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseData<Customer>> findOne(@PathVariable("id") UUID id) {
        ResponseData<Customer> responseData = new ResponseData<>();

        Customer customer = customerService.findOne(id);

        if (customer == null) {
            responseData.setStatus(false);
            responseData.getMessages().add("Customer not found");

            return ResponseEntity.badRequest().body(responseData);
        }

        responseData.setPayload(customer);
        responseData.setStatus(true);
        responseData.getMessages().add("Successfully get customer by ID " + id);

        return ResponseEntity.ok(responseData);

    }

    @PostMapping
    public ResponseEntity<ResponseData<Customer>> create(@Valid @RequestBody Customer customer, Errors errors) {
        ResponseData<Customer> responseData = new ResponseData<>();

        if (errors.hasErrors()) {
            for (ObjectError error : errors.getAllErrors()) {
                responseData.getMessages().add(error.getDefaultMessage());
            }
            responseData.setStatus(false);
            responseData.setPayload(null);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseData);
        }
        try {
            Customer customer2 = modelMapper.map(customer, Customer.class);
            responseData.setStatus(true);
            responseData.setPayload(customerService.save(customer2));
            return ResponseEntity.ok(responseData);
        } catch (Exception e) {
            responseData.setStatus(false);
            responseData.getMessages().add("Categoty name " + customer.getFirst_name() + " not available");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseData);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseData<Customer>> update(@PathVariable("id") UUID id,
            @Valid @RequestBody Customer customer, Errors errors) {
        ResponseData<Customer> responseData = new ResponseData<>();

        if (errors.hasErrors()) {
            for (ObjectError error : errors.getAllErrors()) {
                responseData.getMessages().add(error.getDefaultMessage());
            }
            responseData.setStatus(false);
            responseData.setPayload(null);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseData);
        }
        try {
            Customer customer2 = customerService.findOne(id);

            if (customer2 != null) {
                customer2.setFirst_name(customer.getFirst_name());
                customer2.setLast_name(customer.getLast_name());
                customer2.setEmail(customer.getEmail());
                customer2.setMessage(customer.getMessage());
                customer2.setCountry(customer.getCountry());
                customer2.setPhone_number(customer.getPhone_number());
                customer2.setCompany_name(customer.getCompany_name());
            }

            responseData.setStatus(true);
            responseData.setPayload(customerService.save(customer2));
            return ResponseEntity.ok(responseData);
        } catch (Exception e) {
            responseData.setStatus(false);
            responseData.getMessages().add("Categoty name " + customer.getFirst_name() + " not available");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseData);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseData<Customer>> deleteById(@PathVariable("id") UUID id) {
        ResponseData<Customer> responseData = new ResponseData<>();

        try {
            customerService.deleteById(id);

            responseData.setStatus(true);
            responseData.getMessages().add("Successfully delete customer by ID" + id);

            return ResponseEntity.ok(responseData);
        } catch (Exception e) {
            responseData.setStatus(false);
            responseData.getMessages().add("Customer by ID " + id + " not exist");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseData);
        }

    }

}
