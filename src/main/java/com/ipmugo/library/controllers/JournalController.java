package com.ipmugo.library.controllers;

import java.util.ArrayList;
import java.util.List;
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
import com.ipmugo.library.data.Journal;
import com.ipmugo.library.dto.CategoryData;
import com.ipmugo.library.dto.JournalData;
import com.ipmugo.library.dto.ResponseData;
import com.ipmugo.library.service.CategoryService;
import com.ipmugo.library.service.JournalService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/journal")
public class JournalController {

    @Autowired
    private JournalService journalService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private ModelMapper modelMapper;

    @GetMapping
    public ResponseEntity<ResponseData<Iterable<Journal>>> findAll() {
        ResponseData<Iterable<Journal>> responseData = new ResponseData<>();

        Iterable<Journal> journal = journalService.findAll();

        responseData.setStatus(true);
        responseData.setPayload(journal);
        return ResponseEntity.ok(responseData);

    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseData<Journal>> findOne(@PathVariable("id") UUID id) {
        ResponseData<Journal> responseData = new ResponseData<>();

        Journal journal = journalService.findOne(id);

        if (journal == null) {
            responseData.setStatus(false);
            responseData.getMessages().add("Journal not found");

            return ResponseEntity.badRequest().body(responseData);
        }

        responseData.setPayload(journal);
        responseData.setStatus(true);
        responseData.getMessages().add("Successfully get journal by ID " + id);

        return ResponseEntity.ok(responseData);

    }

    @PostMapping
    public ResponseEntity<ResponseData<Journal>> create(@Valid @RequestBody JournalData journalData, Errors errors) {
        ResponseData<Journal> responseData = new ResponseData<>();

        if (errors.hasErrors()) {
            for (ObjectError error : errors.getAllErrors()) {
                responseData.getMessages().add(error.getDefaultMessage());
            }
            responseData.setStatus(false);
            responseData.setPayload(null);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseData);
        }
        try {
            Journal journal = modelMapper.map(journalData, Journal.class);

            responseData.setStatus(true);
            responseData.setPayload(journalService.save(journal));
            return ResponseEntity.ok(responseData);
        } catch (Exception e) {
            responseData.setStatus(false);
            responseData.getMessages().add("Journal name " + journalData.getName() + " not available");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseData);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseData<Journal>> deleteById(@PathVariable("id") UUID id) {
        ResponseData<Journal> responseData = new ResponseData<>();

        try {
            journalService.deleteById(id);

            responseData.setStatus(true);
            responseData.getMessages().add("Successfully delete journal by ID" + id);

            return ResponseEntity.ok(responseData);
        } catch (Exception e) {
            responseData.setStatus(false);
            responseData.getMessages().add("Journal by ID " + id + " not exist");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseData);
        }

    }

    @PostMapping("/{id}")
    public ResponseEntity<ResponseData<Journal>> setCategory(@Valid @RequestBody CategoryData categoryData,
            @PathVariable("id") UUID id) {
        ResponseData<Journal> responseData = new ResponseData<>();

        try {
            Category category = categoryService.findOne(categoryData.getId());

            if (category == null) {
                responseData.setStatus(false);
                responseData.getMessages().add("Category not found");

                return ResponseEntity.badRequest().body(responseData);
            }

            responseData.setPayload(journalService.setCategory(id, category));
            responseData.setStatus(true);
            responseData.getMessages().add("Successfully set journal category by ID " + id);

            return ResponseEntity.ok(responseData);
        } catch (Exception e) {
            responseData.setStatus(false);
            responseData.getMessages().add(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseData);
        }
    }

    @PostMapping("/collections/{id}")
    public ResponseEntity<ResponseData<Journal>> setCategories(@Valid @RequestBody ArrayList<UUID> categoryData,
            @PathVariable("id") UUID id) {
        ResponseData<Journal> responseData = new ResponseData<>();

        try {
            List<Category> category = categoryService.findList(categoryData);

            if (category.size() == 0) {
                responseData.setStatus(false);
                responseData.getMessages().add("Category not found");

                return ResponseEntity.badRequest().body(responseData);
            }

            responseData.setPayload(journalService.setCategories(id, category));
            responseData.setStatus(true);
            responseData.getMessages().add("Successfully set journal category by ID " + id);

            return ResponseEntity.ok(responseData);
        } catch (Exception e) {
            responseData.setStatus(false);
            responseData.getMessages().add(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseData);
        }
    }
}