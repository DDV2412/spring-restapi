package com.ipmugo.library.data;

import java.util.Date;
import java.util.UUID;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class LogSearch {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String search;

    private Date updated_at;

    private Date created_at;
}
