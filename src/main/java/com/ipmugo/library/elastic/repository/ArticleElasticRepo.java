package com.ipmugo.library.elastic.repository;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.ipmugo.library.elastic.data.ArticleElastic;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.SortOptions;
import co.elastic.clients.elasticsearch._types.SortOrder;
import co.elastic.clients.elasticsearch._types.aggregations.Aggregation;
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

    public SearchResponse<ArticleElastic> findAll(int size, int page, String sortByRelevance, String sortByTitle,
            String sortByPublishAt, String sortByCited) {
        try {

            SortOptions sortBy;

            if (sortByTitle != null) {
                if (sortByTitle == "DESC") {
                    sortBy = new SortOptions.Builder().field(f -> f.field("title").order(SortOrder.Desc)).build();
                } else {
                    sortBy = new SortOptions.Builder().field(f -> f.field("title").order(SortOrder.Asc)).build();
                }
            } else if (sortByCited != null) {
                if (sortByCited == "DESC") {
                    sortBy = new SortOptions.Builder()
                            .field(f -> f.field("sortByCited.references_count").order(SortOrder.Desc)).build();
                } else {
                    sortBy = new SortOptions.Builder()
                            .field(f -> f.field("sortByCited.references_count").order(SortOrder.Asc)).build();
                }
            } else if (sortByPublishAt != null) {
                if (sortByPublishAt == "DESC") {
                    sortBy = new SortOptions.Builder()
                            .field(f -> f.field("publish_date").order(SortOrder.Desc)).build();
                } else {
                    sortBy = new SortOptions.Builder()
                            .field(f -> f.field("publish_date").order(SortOrder.Asc)).build();
                }
            } else if (sortByRelevance != null) {
                if (sortByRelevance == "DESC") {
                    sortBy = new SortOptions.Builder()
                            .field(f -> f.field("_score").order(SortOrder.Desc)).build();
                } else {
                    sortBy = new SortOptions.Builder()
                            .field(f -> f.field("_score").order(SortOrder.Asc)).build();
                }
            } else {
                sortBy = new SortOptions.Builder()
                        .field(f -> f.field("_score").order(SortOrder.Desc)).build();
            }

            SearchResponse<ArticleElastic> searchResponse = client.search(s -> s
                    .index(indexName)
                    .size(size)
                    .from(page)
                    .sort(sortBy)
                    .aggregations(new HashMap<>() {
                        {
                            put("journal_name", new Aggregation.Builder()
                                    .terms(new TermsAggregation.Builder().field("journal.name").build())
                                    .build());
                        }
                    }).aggregations(new HashMap<>() {
                        {
                            put("set_spec", new Aggregation.Builder()
                                    .terms(new TermsAggregation.Builder().field("set_spec").build())
                                    .build());
                        }
                    })
                    .aggregations(new HashMap<>() {
                        {
                            put("year", new Aggregation.Builder()
                                    .terms(new TermsAggregation.Builder().field("publish_year").build())
                                    .build());
                        }
                    }),
                    ArticleElastic.class);

            return searchResponse;
        } catch (Exception e) {
            throw new IllegalArgumentException(e.getMessage(), null);
        }
    }

    public SearchResponse<ArticleElastic> findByDoi(String doi) {
        try {
            SearchResponse<ArticleElastic> response = client.search(s -> s
                    .index(indexName)
                    .query(q -> q
                            .match(t -> t
                                    .field("doi")
                                    .query(doi))),
                    ArticleElastic.class);

            return response;
        } catch (Exception e) {
            throw new IllegalArgumentException(e.getMessage(), null);
        }
    }

}
