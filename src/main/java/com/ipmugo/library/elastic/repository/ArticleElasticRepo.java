package com.ipmugo.library.elastic.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.ipmugo.library.elastic.data.ArticleElastic;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.IndexResponse;
import co.elastic.clients.elasticsearch.core.SearchTemplateResponse;

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

        public SearchTemplateResponse<ArticleElastic> search(String query) {
                try {

                        client.putScript(r -> r
                                        .id("query-script")
                                        .script(s -> s
                                                        .lang("mustache")
                                                        .source(query)));

                        SearchTemplateResponse<ArticleElastic> searchResponse = client.searchTemplate(r -> r
                                        .index(indexName)
                                        .id("query-script"),
                                        ArticleElastic.class);

                        return searchResponse;

                } catch (Exception e) {
                        throw new IllegalArgumentException(e.getMessage(), null);
                }
        }

}
