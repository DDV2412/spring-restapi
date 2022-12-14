package com.ipmugo.library.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ipmugo.library.dto.ResponseData;
import com.ipmugo.library.elastic.data.ArticleElastic;
import com.ipmugo.library.elastic.service.ElasticSearchService;

@RestController
@RequestMapping("/api/search")
public class ElasticController {

    @Autowired
    private ElasticSearchService elasticSearchService;

    @GetMapping
    public ResponseEntity<ResponseData<List<ArticleElastic>>> findAll(@RequestParam(defaultValue = "15") int size) {
        ResponseData<List<ArticleElastic>> responseData = new ResponseData<>();

        responseData.setStatus(true);
        responseData.setPayload(elasticSearchService.findAll(size));
        return ResponseEntity.ok(responseData);
    }

}
