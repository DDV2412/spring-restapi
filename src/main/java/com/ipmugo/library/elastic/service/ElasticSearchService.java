package com.ipmugo.library.elastic.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ipmugo.library.elastic.data.ArticleElastic;
import com.ipmugo.library.elastic.repository.ArticleElasticRepo;

import co.elastic.clients.elasticsearch._types.SortOptions;
import co.elastic.clients.elasticsearch.core.IndexResponse;
import co.elastic.clients.elasticsearch.core.SearchResponse;

@Service
public class ElasticSearchService {

    @Autowired
    private ArticleElasticRepo articleElasticRepo;

    public IndexResponse save(ArticleElastic article) {
        return articleElasticRepo.save(article);
    }

    public SearchResponse<ArticleElastic> findAll(int size, int page, SortOptions sortBy) {
        return articleElasticRepo.findAll(size, page, sortBy);
    }

    public SearchResponse<ArticleElastic> findByDoi(String doi) {
        return articleElasticRepo.findByDoi(doi);
    }
}
