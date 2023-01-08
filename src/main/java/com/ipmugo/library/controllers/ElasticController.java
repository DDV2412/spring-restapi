package com.ipmugo.library.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ipmugo.library.dto.ResponseElastic;
import com.ipmugo.library.elastic.data.ArticleElastic;
import com.ipmugo.library.elastic.service.ElasticSearchService;

import co.elastic.clients.elasticsearch._types.aggregations.Aggregate;
import co.elastic.clients.elasticsearch._types.aggregations.MultiBucketBase;
import co.elastic.clients.elasticsearch._types.aggregations.StringTermsBucket;
import co.elastic.clients.elasticsearch.core.SearchTemplateResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import co.elastic.clients.elasticsearch.core.search.TotalHits;

@RestController
@RequestMapping("/api/search")
public class ElasticController {

        @Autowired
        private ElasticSearchService elasticSearchService;

        @PostMapping
        public ResponseEntity<ResponseElastic<List<ArticleElastic>>> search(
                        @RequestBody String query) {
                ResponseElastic<List<ArticleElastic>> responseData = new ResponseElastic<>();

                query = query.replaceAll("\r\n", "");

                SearchTemplateResponse<ArticleElastic> searchResponse = elasticSearchService.search(query);

                List<Hit<ArticleElastic>> hits = searchResponse.hits().hits();

                TotalHits total = searchResponse.hits().total();

                if (total != null) {
                        responseData.setTotalValue(total.value());
                }

                Map<String, Aggregate> aggrs = searchResponse.aggregations();

                List<Map<String, Long>> aggrList = new ArrayList<>();

                for (Map.Entry<String, Aggregate> entry : aggrs.entrySet()) {
                        aggrList.add(aggrs.get(entry.getKey())
                                        .sterms().buckets().array().stream()
                                        .collect(Collectors.toMap(StringTermsBucket::key,
                                                        MultiBucketBase::docCount))
                                        .entrySet()
                                        .stream()
                                        .collect(Collectors.toMap(e -> e.getKey().stringValue(),
                                                        Map.Entry::getValue)));
                }

                List<ArticleElastic> articles = new ArrayList<>();
                for (Hit<ArticleElastic> object : hits) {
                        articles.add((ArticleElastic) object.source());
                }

                responseData.setStatus(true);
                responseData.setPayload(articles);
                responseData.setAggregations(aggrList);

                return ResponseEntity.ok(responseData);
        }

        @GetMapping("/feature_article")
        public ResponseEntity<ResponseElastic<List<ArticleElastic>>> articleFeatures(@RequestBody String query) {
                ResponseElastic<List<ArticleElastic>> responseData = new ResponseElastic<>();

                query = query.replaceAll("\r\n", "");
                SearchTemplateResponse<ArticleElastic> searchResponse = elasticSearchService.search(query);

                List<Hit<ArticleElastic>> hits = searchResponse.hits().hits();

                TotalHits total = searchResponse.hits().total();

                if (total != null) {
                        responseData.setTotalValue(total.value());
                }

                List<ArticleElastic> articles = new ArrayList<>();
                for (Hit<ArticleElastic> object : hits) {
                        articles.add((ArticleElastic) object.source());
                }

                responseData.setStatus(true);
                responseData.setPayload(articles);

                return ResponseEntity.ok(responseData);
        }

        @GetMapping("/feature_author")
        public ResponseEntity<ResponseElastic<List<ArticleElastic>>> authorFeatures(@RequestBody String query) {
                ResponseElastic<List<ArticleElastic>> responseData = new ResponseElastic<>();

                SearchTemplateResponse<ArticleElastic> searchResponse = elasticSearchService.search(query);

                List<Hit<ArticleElastic>> hits = searchResponse.hits().hits();

                TotalHits total = searchResponse.hits().total();

                if (total != null) {
                        responseData.setTotalValue(total.value());
                }

                List<ArticleElastic> articles = new ArrayList<>();
                for (Hit<ArticleElastic> object : hits) {
                        articles.add((ArticleElastic) object.source());
                }

                responseData.setStatus(true);
                responseData.setPayload(articles);

                return ResponseEntity.ok(responseData);
        }

}
