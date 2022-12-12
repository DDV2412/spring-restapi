package com.ipmugo.library.data;

import java.sql.Timestamp;
import java.util.Date;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "customer")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(length = 255, nullable = false)
    private String first_name;

    @Column(length = 255, nullable = false)
    private String last_name;

    @Column(length = 255, nullable = false)
    private String company_name;

    @Column(length = 255, nullable = false)
    private String email;

    @Column(length = 255, nullable = false)
    private Integer phone_number;

    @Column(length = 255, nullable = false)
    private String country;

    @Column(columnDefinition = "TEXT")
    private String message;

    @CreationTimestamp
    private Timestamp updatedAt;

    @CreationTimestamp
    private Timestamp createdAt;

    public Customer() {
    }

    public Customer(UUID id, String first_name, String last_name, String company_name, String email,
            Integer phone_number, String country, String message, Timestamp updatedAt, Timestamp createdAt) {
        this.id = id;
        this.first_name = first_name;
        this.last_name = last_name;
        this.company_name = company_name;
        this.email = email;
        this.phone_number = phone_number;
        this.country = country;
        this.message = message;
        this.updatedAt = updatedAt;
        this.createdAt = createdAt;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getCompany_name() {
        return company_name;
    }

    public void setCompany_name(String company_name) {
        this.company_name = company_name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(Integer phone_number) {
        this.phone_number = phone_number;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Date getupdatedAt() {
        return updatedAt;
    }

    public void setupdatedAt(Timestamp updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Date getcreatedAt() {
        return createdAt;
    }

    public void setcreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

}
