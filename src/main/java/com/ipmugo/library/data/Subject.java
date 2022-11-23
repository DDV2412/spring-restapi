package com.ipmugo.library.data;

import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Entity
public class Subject {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "subject_name", unique = true, length = 36)
    @NotBlank(message = "Subject name can not blank")
    @Size(message = "Subject name length max must 36 characters")
    private String name;

    public Subject() {
    }

    public Subject(UUID id,
            @NotBlank(message = "Subject name can not blank") @Size(message = "Subject name length max must 36 characters") String name) {
        this.id = id;
        this.name = name;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
