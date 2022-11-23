package com.ipmugo.library.data;

import java.util.Date;
import java.util.UUID;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    // private String username;

    // private String first_name;

    // private String last_name;

    // private String email;

    // private String password;

    // private String isEnabled;

    // private String isRole;

    @Temporal(TemporalType.TIMESTAMP)
    private Date updated_at;

    @Temporal(TemporalType.TIMESTAMP)
    private Date created_at;

}
