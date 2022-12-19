package com.ipmugo.library.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ipmugo.library.dto.ResponseElastic;
import com.ipmugo.library.elastic.data.ArticleElastic;
import com.ipmugo.library.elastic.service.ElasticSearchService;

import co.elastic.clients.elasticsearch._types.aggregations.MultiBucketBase;
import co.elastic.clients.elasticsearch._types.aggregations.StringTermsBucket;
import co.elastic.clients.elasticsearch.core.SearchTemplateResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;

@RestController
@RequestMapping("/api/search")
public class ElasticController {

        @Autowired
        private ElasticSearchService elasticSearchService;

        @GetMapping
        public ResponseEntity<ResponseElastic<List<ArticleElastic>>> search(
                        @RequestParam(defaultValue = "15") String size,
                        @RequestParam(defaultValue = "0") String page,
                        @RequestParam(required = false) String sortByRelevance,
                        @RequestParam(required = false) String sortByTitle,
                        @RequestParam(required = false) String sortByPublishAt,
                        @RequestParam(required = false) String sortByCited,
                        @RequestParam(required = false) String search, @RequestBody(required = false) String query) {
                ResponseElastic<List<ArticleElastic>> responseData = new ResponseElastic<>();

                String sortBy;
                if (sortByTitle != null) {
                        if (sortByTitle.equals("desc")) {
                                sortBy = "{\"title.keyword\": {\"order\": \"desc\"}";
                        } else {
                                sortBy = "{\"title.keyword\": {\"order\": \"asc\"}";
                        }
                } else if (sortByCited != null) {
                        if (sortByCited.equals("desc")) {
                                sortBy = "{\"sortByCited.references_count\": {\"order\": \"desc\"}";
                        } else {
                                sortBy = "{\"sortByCited.references_count\": {\"order\": \"asc\"}";
                        }
                } else if (sortByPublishAt != null) {
                        if (sortByPublishAt.equals("desc")) {
                                sortBy = "{\"publish_date\": {\"order\": \"desc\",\"format\":\"strict_date_optional_time_nanos\"}";
                        } else {
                                sortBy = "{\"publish_date\": {\"order\": \"asc\",\"format\":\"strict_date_optional_time_nanos\"}";
                        }
                } else if (sortByRelevance != null) {
                        if (sortByRelevance.equals("desc")) {
                                sortBy = "{\"_score\": {\"order\": \"desc\"}";
                        } else {
                                sortBy = "{\"_score\": {\"order\": \"asc\"}";
                        }
                } else {
                        sortBy = "{\"publish_date\": {\"order\": \"desc\",\"format\":\"strict_date_optional_time_nanos\"}";
                }

                if (query != null) {
                        query = query.replaceAll("\r\n", "");
                } else {
                        query = "{\"from\":\"" + page + "\",\"size\": \"" + size
                                        + "\",\"query\": {\"match_all\": {}},\"sort\": " + sortBy
                                        + "},    \"aggs\": {\"journal_name\": {\"terms\": {\"field\": \"journal.name.keyword\"}        },        \"year\": {\"terms\": {\"field\": \"publish_year.keyword\"}},\"set_spec\": {\"terms\": {\"field\": \"set_spec.keyword\"}}}}";
                }

                SearchTemplateResponse<ArticleElastic> searchResponse = elasticSearchService.search(query);

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
                                .collect(Collectors.toMap(StringTermsBucket::key, MultiBucketBase::docCount)).entrySet()
                                .stream()
                                .collect(Collectors.toMap(entry -> entry.getKey().stringValue(), Map.Entry::getValue)));
                aggrList.add(set_spec.stream()
                                .collect(Collectors.toMap(StringTermsBucket::key, MultiBucketBase::docCount)).entrySet()
                                .stream()
                                .collect(Collectors.toMap(entry -> entry.getKey().stringValue(), Map.Entry::getValue)));
                aggrList.add(year.stream()
                                .collect(Collectors.toMap(StringTermsBucket::key, MultiBucketBase::docCount)).entrySet()
                                .stream()
                                .collect(Collectors.toMap(entry -> entry.getKey().stringValue(), Map.Entry::getValue)));

                responseData.setStatus(true);
                responseData.setPayload(articles);
                responseData.setAggregations(aggrList);
                return ResponseEntity.ok(responseData);
        }

}
