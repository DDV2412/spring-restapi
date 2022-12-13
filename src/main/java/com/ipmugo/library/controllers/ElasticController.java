package com.ipmugo.library.controllers;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ipmugo.library.elastic.data.ArticleElastic;
import com.ipmugo.library.elastic.repository.ArticleElasticRepo;

@RestController
@RequestMapping("/api/search")
public class ElasticController {

    @Autowired
    private ArticleElasticRepo articleElasticRepo;

    @GetMapping
    public ResponseEntity<Object> searchAllDocument() throws IOException {
        Iterable<ArticleElastic> articles = articleElasticRepo.findAll();
        return new ResponseEntity<>(articles, HttpStatus.OK);
    }

}
