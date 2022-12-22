package com.ipmugo.library.elastic.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.ipmugo.library.elastic.data.ArticleElastic;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.aggregations.Aggregation;
import co.elastic.clients.elasticsearch._types.aggregations.NestedAggregation;
import co.elastic.clients.elasticsearch._types.aggregations.TermsAggregation;
import co.elastic.clients.elasticsearch.core.IndexResponse;
import co.elastic.clients.elasticsearch.core.SearchResponse;

@Repository
public class ArticleElasticRepo {

        @Autowired
        private ElasticsearchClient client;

        private final String indexName = "articles";

        public IndexResponse save(ArticleElastic article) {
                try {
                        IndexResponse response = client.index(i -> i
                                        .index(indexName)
                                        .id(article.getId())
                                        .document(article));
                        return response;
                } catch (Exception e) {
                        throw new IllegalArgumentException(e.getMessage(), null);
                }
        }

        public SearchResponse<ArticleElastic> search(int page, int size) {
                try {

                        SearchResponse<ArticleElastic> searchResponse = client.search(r -> r
                                        .index(indexName)
                                        .from(page)
                                        .size(size)
                                        .aggregations("journal_name", new Aggregation.Builder()
                                                        .terms(new TermsAggregation.Builder()
                                                                        .field("journal.name.keyword").build())
                                                        .build())
                                        .aggregations("set_spec", new Aggregation.Builder()
                                                        .terms(new TermsAggregation.Builder().field("set_spec.keyword")
                                                                        .build())
                                                        .build())
                                        .aggregations("year", new Aggregation.Builder()
                                                        .terms(new TermsAggregation.Builder()
                                                                        .field("publish_year.keyword").build())
                                                        .build())
                                        .aggregations("publisher", new Aggregation.Builder()
                                                        .terms(new TermsAggregation.Builder().field("publisher.keyword")
                                                                        .build())
                                                        .build())
                                        .aggregations("subjects", new Aggregation.Builder()
                                                        .nested(new NestedAggregation.Builder().path("subjects")
                                                                        .build())
                                                        .aggregations("subject_name", new Aggregation.Builder()
                                                                        .terms(new TermsAggregation.Builder()
                                                                                        .field("subject.name.keyword")
                                                                                        .missing("N/A")
                                                                                        .build())
                                                                        .build())
                                                        .build()),
                                        ArticleElastic.class);

                        return searchResponse;

                } catch (Exception e) {
                        throw new IllegalArgumentException(e.getMessage(), null);
                }
        }

}
