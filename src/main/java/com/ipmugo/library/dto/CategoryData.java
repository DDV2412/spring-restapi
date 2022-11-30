package com.ipmugo.library.dto;

import java.util.UUID;

import jakarta.validation.constraints.NotEmpty;

public class CategoryData {

    private UUID id;

    @NotEmpty(message = "Category name cannot be an empty field")
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

}
