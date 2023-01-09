package com.ipmugo.library.controllers;

import java.util.List;
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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ipmugo.library.data.Author;
import com.ipmugo.library.dto.ResponseData;
import com.ipmugo.library.service.AuthorService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/author")
public class AuthorController {

    @Autowired
    private AuthorService authorService;

    @Autowired
    private ModelMapper modelMapper;

    @GetMapping
    public ResponseEntity<ResponseData<Iterable<Author>>> findAll(@RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "15") int size) {
        ResponseData<Iterable<Author>> responseData = new ResponseData<>();

        Pageable pageable = PageRequest.of(page, size);

        Page<Author> author = authorService.findAll(pageable);

        responseData.setStatus(true);
        responseData.setPayload(author.getContent());
        return ResponseEntity.ok(responseData);

    }

    @GetMapping("/{first_name}/{last_name}")
    public ResponseEntity<ResponseData<Iterable<Author>>> findAllByQuery(@PathVariable("first_name") String first_name,
            @PathVariable("last_name") String last_name, @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "15") int size) {
        ResponseData<Iterable<Author>> responseData = new ResponseData<>();

        Pageable pageable = PageRequest.of(page, size);

        Page<Author> author = authorService.findByQuery(pageable, first_name, last_name);

        responseData.setStatus(true);
        responseData.setPayload(author.getContent());
        return ResponseEntity.ok(responseData);

    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseData<Author>> findOne(@PathVariable("id") UUID id) {
        ResponseData<Author> responseData = new ResponseData<>();

        Author author = authorService.findOne(id);

        if (author == null) {
            responseData.setStatus(false);
            responseData.getMessages().add("Author not found");

            return ResponseEntity.badRequest().body(responseData);
        }

        responseData.setPayload(author);
        responseData.setStatus(true);
        responseData.getMessages().add("Successfully get author by ID " + id);

        return ResponseEntity.ok(responseData);

    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseData<Author>> update(@PathVariable("id") UUID id, @Valid @RequestBody Author author,
            Errors errors) {
        ResponseData<Author> responseData = new ResponseData<>();

        if (errors.hasErrors()) {
            for (ObjectError error : errors.getAllErrors()) {
                responseData.getMessages().add(error.getDefaultMessage());
            }
            responseData.setStatus(false);
            responseData.setPayload(null);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseData);
        }
        try {
            Author author2 = authorService.findOne(id);

            if (author2 == null) {
                responseData.setStatus(false);
                responseData.getMessages().add("Author with " + id + " not found");

                return ResponseEntity.badRequest().body(responseData);
            }

            Author authorMapper = modelMapper.map(author, Author.class);

            author2.setFirstName(authorMapper.getFirstName());
            author2.setLastName(authorMapper.getLastName());
            author2.setEmail(authorMapper.getEmail());
            author2.setOrcid(authorMapper.getOrcid());
            author2.setScopus_id(authorMapper.getScopus_id());
            author2.setGoogle_scholar(authorMapper.getGoogle_scholar());
            author2.setAffiliation(authorMapper.getAffiliation());

            responseData.setStatus(true);
            responseData.setPayload(authorService.save(author2));
            return ResponseEntity.ok(responseData);
        } catch (Exception e) {
            responseData.setStatus(false);
            responseData.getMessages().add("Author name " + author.getFirstName() + " not available");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseData);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseData<Author>> deleteById(@PathVariable("id") UUID id) {
        ResponseData<Author> responseData = new ResponseData<>();

        try {
            authorService.deleteById(id);

            responseData.setStatus(true);
            responseData.getMessages().add("Successfully delete author by ID" + id);

            return ResponseEntity.ok(responseData);
        } catch (Exception e) {
            responseData.setStatus(false);
            responseData.getMessages().add("Author by ID " + id + " not exist");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseData);
        }

    }

    @PutMapping()
    public ResponseEntity<ResponseData<Iterable<Author>>> updateAll(@Valid @RequestBody List<Author> authors,
            Errors errors) {
        ResponseData<Iterable<Author>> responseData = new ResponseData<>();

        if (errors.hasErrors()) {
            for (ObjectError error : errors.getAllErrors()) {
                responseData.getMessages().add(error.getDefaultMessage());
            }
            responseData.setStatus(false);
            responseData.setPayload(null);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseData);
        }
        try {

            for (Author author : authors) {
                Author author2 = authorService.findOne(author.getId());

                author2.setFirstName(author.getFirstName());
                author2.setLastName(author.getLastName());
                author2.setEmail(author.getEmail());
                author2.setOrcid(author.getOrcid());
                author2.setScopus_id(author.getScopus_id());
                author2.setGoogle_scholar(author.getGoogle_scholar());
                author2.setAffiliation(author.getAffiliation());
                author2.setPhoto_profile(author.getPhoto_profile());

                authorService.save(author2);
            }
            responseData.setStatus(true);
            responseData.setPayload(authors);
            return ResponseEntity.ok(responseData);
        } catch (Exception e) {
            responseData.setStatus(false);
            responseData.getMessages().add(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseData);
        }
    }
}
