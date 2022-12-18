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

import com.ipmugo.library.dto.ResponseData;
import com.ipmugo.library.dto.ResponseElastic;
import com.ipmugo.library.elastic.data.ArticleElastic;
import com.ipmugo.library.elastic.service.ElasticSearchService;

import co.elastic.clients.elasticsearch._types.SortOptions;
import co.elastic.clients.elasticsearch._types.SortOrder;
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
            @RequestParam(defaultValue = "0") int page, @RequestParam(required = false) String sortByRelevance,
            @RequestParam(required = false) String sortByTitle,
            @RequestParam(required = false) String sortByPublishAt,
            @RequestParam(required = false) String sortByCited) {
        ResponseElastic<List<ArticleElastic>> responseData = new ResponseElastic<>();

        SortOptions sortBy;

        if (sortByTitle != null) {
            if (sortByTitle.equals("desc")) {
                sortBy = new SortOptions.Builder().field(f -> f.field("title.keyword").order(SortOrder.Desc)).build();
            } else {
                sortBy = new SortOptions.Builder().field(f -> f.field("title.keyword").order(SortOrder.Asc)).build();
            }
        } else if (sortByCited != null) {
            if (sortByCited.equals("desc")) {
                sortBy = new SortOptions.Builder()
                        .field(f -> f.field("sortByCited.references_count").order(SortOrder.Desc)).build();
            } else {
                sortBy = new SortOptions.Builder()
                        .field(f -> f.field("sortByCited.references_count").order(SortOrder.Asc)).build();
            }
        } else if (sortByPublishAt != null) {
            if (sortByPublishAt.equals("desc")) {
                sortBy = new SortOptions.Builder()
                        .field(f -> f.field("publish_date").order(SortOrder.Desc)).build();
            } else {
                sortBy = new SortOptions.Builder()
                        .field(f -> f.field("publish_date").order(SortOrder.Asc)).build();
            }
        } else if (sortByRelevance != null) {
            if (sortByRelevance.equals("desc")) {
                sortBy = new SortOptions.Builder()
                        .field(f -> f.field("_score").order(SortOrder.Desc)).build();
            } else {
                sortBy = new SortOptions.Builder()
                        .field(f -> f.field("_score").order(SortOrder.Asc)).build();
            }
        } else {
            sortBy = new SortOptions.Builder()
                    .field(f -> f.field("publish_date").order(SortOrder.Desc)).build();
        }

        SearchResponse<ArticleElastic> searchResponse = elasticSearchService.findAll(size, page, sortBy);

        List<Hit<ArticleElastic>> hits = searchResponse.hits().hits();

        List<ArticleElastic> articles = new ArrayList<>();
        for (Hit<ArticleElastic> object : hits) {
            articles.add((ArticleElastic) object.source());
        }

        List<Map<String, Long>> aggrList = new ArrayList<>();

        var journal_name = searchResponse.aggregations().get("journal_name").sterms().buckets().array();
        var set_spec = searchResponse.aggregations().get("set_spec").sterms().buckets().array();
        var year = searchResponse.aggregations().get("year").sterms().buckets().array();

        aggrList.add(journal_name.stream()
                .collect(Collectors.toMap(StringTermsBucket::key, MultiBucketBase::docCount)).entrySet().stream()
                .collect(Collectors.toMap(entry -> entry.getKey().stringValue(), Map.Entry::getValue)));
        aggrList.add(set_spec.stream()
                .collect(Collectors.toMap(StringTermsBucket::key, MultiBucketBase::docCount)).entrySet().stream()
                .collect(Collectors.toMap(entry -> entry.getKey().stringValue(), Map.Entry::getValue)));
        aggrList.add(year.stream()
                .collect(Collectors.toMap(StringTermsBucket::key, MultiBucketBase::docCount)).entrySet().stream()
                .collect(Collectors.toMap(entry -> entry.getKey().stringValue(), Map.Entry::getValue)));

        responseData.setStatus(true);
        responseData.setPayload(articles);
        responseData.setAggregations(aggrList);
        return ResponseEntity.ok(responseData);
    }

    @GetMapping("/{unique}/{doi}")
    public ResponseEntity<ResponseData<ArticleElastic>> findByDoi(@PathVariable("unique") String unique,
            @PathVariable("doi") String doi) {
        ResponseData<ArticleElastic> responseData = new ResponseData<>();

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
