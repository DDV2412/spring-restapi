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

import com.ipmugo.library.data.Article;
import com.ipmugo.library.dto.ArticleData;
import com.ipmugo.library.dto.ResponseData;
import com.ipmugo.library.service.ArticleService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/article")
public class ArticleController {

    @Autowired
    private ArticleService articleService;

    @Autowired
    private ModelMapper modelMapper;

    @GetMapping
    public ResponseEntity<ResponseData<Iterable<Article>>> findAll() {
        ResponseData<Iterable<Article>> responseData = new ResponseData<>();

        Iterable<Article> article = articleService.findAll();

        responseData.setStatus(true);
        responseData.setPayload(article);
        return ResponseEntity.ok(responseData);

    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseData<Article>> findOne(@PathVariable("id") UUID id) {
        ResponseData<Article> responseData = new ResponseData<>();

        Article article = articleService.findOne(id);

        if (article == null) {
            responseData.setStatus(false);
            responseData.getMessages().add("Article not found");

            return ResponseEntity.badRequest().body(responseData);
        }

        responseData.setPayload(article);
        responseData.setStatus(true);
        responseData.getMessages().add("Successfully get article by ID " + id);

        return ResponseEntity.ok(responseData);

    }

    @PostMapping
    public ResponseEntity<ResponseData<Article>> create(@Valid @RequestBody ArticleData articleData, Errors errors) {
        ResponseData<Article> responseData = new ResponseData<>();

        if (errors.hasErrors()) {
            for (ObjectError error : errors.getAllErrors()) {
                responseData.getMessages().add(error.getDefaultMessage());
            }
            responseData.setStatus(false);
            responseData.setPayload(null);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseData);
        }
        try {
            Article article = modelMapper.map(articleData, Article.class);

            responseData.setStatus(true);
            responseData.setPayload(articleService.save(article));
            return ResponseEntity.ok(responseData);
        } catch (Exception e) {
            responseData.setStatus(false);
            responseData.getMessages().add("Article name " + articleData.getTitle() + " not available");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseData);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseData<Article>> deleteById(@PathVariable("id") UUID id) {
        ResponseData<Article> responseData = new ResponseData<>();

        try {
            articleService.deleteById(id);

            responseData.setStatus(true);
            responseData.getMessages().add("Successfully delete article by ID" + id);

            return ResponseEntity.ok(responseData);
        } catch (Exception e) {
            responseData.setStatus(false);
            responseData.getMessages().add("Article by ID " + id + " not exist");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseData);
        }

    }

    @GetMapping("/citation-scopus/{id}")
    public Object setCitationScopus(@PathVariable("id") UUID id) {
        return articleService.citationScopus(id);
    }

    @GetMapping("/citation-crossref/{id}")
    public Object setCitationCrossRef(@PathVariable("id") UUID id) {
        return articleService.citationCrossReff(id);
    }
}
