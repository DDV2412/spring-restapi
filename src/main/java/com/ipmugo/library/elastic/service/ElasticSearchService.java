package com.ipmugo.library.elastic.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ipmugo.library.elastic.data.ArticleElastic;
import com.ipmugo.library.elastic.repository.ArticleElasticRepo;

import co.elastic.clients.elasticsearch.core.IndexResponse;

@Service
public class ElasticSearchService {

    @Autowired
    private ArticleElasticRepo articleElasticRepo;

    public IndexResponse save(ArticleElastic article) {
        return articleElasticRepo.save(article);
    }

    public List<ArticleElastic> findAll(int size) {
        return articleElasticRepo.findAll(size);
    }

}
