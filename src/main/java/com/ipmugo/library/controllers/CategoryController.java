package com.ipmugo.library.controllers;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ipmugo.library.data.Category;
import com.ipmugo.library.dto.ResponseData;
import com.ipmugo.library.service.CategoryService;

@RestController
@RequestMapping("/api/category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @GetMapping
    public ResponseEntity<ResponseData<Iterable<Category>>> findAll() {
        ResponseData<Iterable<Category>> responseData = new ResponseData<>();

        Iterable<Category> category = categoryService.findAll();

        responseData.setStatus(true);
        responseData.setPayload(category);
        return ResponseEntity.ok(responseData);

    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseData<Category>> findOne(@PathVariable("id") UUID id) {
        ResponseData<Category> responseData = new ResponseData<>();

        Category category = categoryService.findOne(id);

        if (category == null) {
            responseData.setStatus(false);
            responseData.getMessages().add("Category not found");

            return ResponseEntity.badRequest().body(responseData);
        }

        responseData.setPayload(category);
        responseData.setStatus(true);
        responseData.getMessages().add("Successfully get category by ID" + id);

        return ResponseEntity.ok(responseData);

    }

}
