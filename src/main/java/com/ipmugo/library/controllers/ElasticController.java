package com.ipmugo.library.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ipmugo.library.dto.ResponseElastic;
import com.ipmugo.library.elastic.data.ArticleElastic;
import com.ipmugo.library.elastic.service.ElasticSearchService;

import co.elastic.clients.elasticsearch._types.FieldValue;
import co.elastic.clients.elasticsearch._types.aggregations.MultiBucketBase;
import co.elastic.clients.elasticsearch._types.aggregations.StringTermsBucket;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;

@RestController
@RequestMapping("/api/search")
public class ElasticController {

    @Autowired
    private ElasticSearchService elasticSearchService;

    @GetMapping
    public ResponseEntity<ResponseElastic<List<ArticleElastic>>> findAll(@RequestParam(defaultValue = "15") int size,
            @RequestParam(defaultValue = "0") int page) {
        ResponseElastic<List<ArticleElastic>> responseData = new ResponseElastic<>();

        SearchResponse<ArticleElastic> searchResponse = elasticSearchService.findAll(size, page);

        List<Hit<ArticleElastic>> hits = searchResponse.hits().hits();

        List<ArticleElastic> articles = new ArrayList<>();
        for (Hit<ArticleElastic> object : hits) {
            articles.add((ArticleElastic) object.source());
        }

        var list = searchResponse.aggregations().get("journal_name").sterms().buckets().array();

        Map<FieldValue, Long> journalName = list.stream()
                .collect(Collectors.toMap(StringTermsBucket::key, MultiBucketBase::docCount));

        Map<String, Long> journalName2 = journalName.entrySet().stream()
                .collect(Collectors.toMap(entry -> entry.getKey().stringValue(), Map.Entry::getValue));

        responseData.setStatus(true);
        responseData.setPayload(articles);
        responseData.setAggregations(journalName2);
        return ResponseEntity.ok(responseData);
    }

    @GetMapping("/{unique}/{doi}")
    public ResponseEntity<ResponseElastic<ArticleElastic>> findByDoi(@PathVariable("unique") String unique,
            @PathVariable("doi") String doi) {
        ResponseElastic<ArticleElastic> responseData = new ResponseElastic<>();

        SearchResponse<ArticleElastic> searchResponse = elasticSearchService.findByDoi(unique + "/" + doi);

        List<Hit<ArticleElastic>> hits = searchResponse.hits().hits();

        List<ArticleElastic> articles = new ArrayList<>();
        for (Hit<ArticleElastic> object : hits) {
            articles.add((ArticleElastic) object.source());
        }

        responseData.setStatus(true);
        responseData.setPayload(articles.get(0));
        return ResponseEntity.ok(responseData);
    }

}
