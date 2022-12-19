package com.ipmugo.library.elastic.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ipmugo.library.elastic.data.ArticleElastic;
import com.ipmugo.library.elastic.repository.ArticleElasticRepo;

import co.elastic.clients.elasticsearch.core.IndexResponse;
import co.elastic.clients.elasticsearch.core.SearchTemplateResponse;

@Service
public class ElasticSearchService {

    @Autowired
    private ArticleElasticRepo articleElasticRepo;

    public IndexResponse save(ArticleElastic article) {
        return articleElasticRepo.save(article);
    }

    public SearchTemplateResponse<ArticleElastic> search(String query) {
        return articleElasticRepo.search(query);
    }

}
