package com.ipmugo.library.elastic.repository;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.ipmugo.library.elastic.data.ArticleElastic;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.IndexResponse;
import co.elastic.clients.elasticsearch.core.SearchRequest;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;

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

    public List<ArticleElastic> findAll(int size) {
        try {
            SearchRequest searchRequest = SearchRequest.of(s -> s.index(indexName).size(size));
            SearchResponse<ArticleElastic> searchResponse = client.search(searchRequest, ArticleElastic.class);

            List<Hit<ArticleElastic>> hits = searchResponse.hits().hits();

            List<ArticleElastic> articles = new ArrayList<>();
            for (Hit<ArticleElastic> object : hits) {

                articles.add((ArticleElastic) object.source());

            }

            return articles;
        } catch (Exception e) {
            throw new IllegalArgumentException(e.getMessage(), null);
        }
    }

}
