package com.ipmugo.library.dto;

import jakarta.validation.constraints.NotEmpty;

public class CategoryData {

    @NotEmpty(message = "Category Name cannot be an empty field")
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
