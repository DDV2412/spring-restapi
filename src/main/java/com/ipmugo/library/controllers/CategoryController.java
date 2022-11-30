package com.ipmugo.library.controllers;

import java.util.UUID;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ipmugo.library.data.Category;
import com.ipmugo.library.dto.CategoryData;
import com.ipmugo.library.dto.ResponseData;
import com.ipmugo.library.service.CategoryService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private ModelMapper modelMapper;

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
        responseData.getMessages().add("Successfully get category by ID " + id);

        return ResponseEntity.ok(responseData);

    }

    @PostMapping
    public ResponseEntity<ResponseData<Category>> create(@Valid @RequestBody CategoryData categoryData, Errors errors) {
        ResponseData<Category> responseData = new ResponseData<>();

        if (errors.hasErrors()) {
            for (ObjectError error : errors.getAllErrors()) {
                responseData.getMessages().add(error.getDefaultMessage());
            }
            responseData.setStatus(false);
            responseData.setPayload(null);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseData);
        }
        try {
            Category category = modelMapper.map(categoryData, Category.class);
            responseData.setStatus(true);
            responseData.setPayload(categoryService.save(category));
            return ResponseEntity.ok(responseData);
        } catch (Exception e) {
            responseData.setStatus(false);
            responseData.getMessages().add(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseData);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseData<Category>> deleteById(@PathVariable("id") UUID id) {
        ResponseData<Category> responseData = new ResponseData<>();

        try {
            categoryService.deleteById(id);

            responseData.setStatus(true);
            responseData.getMessages().add("Successfully delete category by ID" + id);

            return ResponseEntity.ok(responseData);
        } catch (Exception e) {
            responseData.setStatus(false);
            responseData.getMessages().add(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseData);
        }

    }

}
