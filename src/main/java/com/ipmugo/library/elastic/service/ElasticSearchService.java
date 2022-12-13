package com.ipmugo.library.elastic.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ipmugo.library.elastic.data.ArticleElastic;
import com.ipmugo.library.elastic.repository.ArticleElasticRepo;

@Service
public class ElasticSearchService {

    @Autowired
    private ArticleElasticRepo articleElasticRepo;

    public void createProductIndexBulk(final List<ArticleElastic> articles) {
        articleElasticRepo.saveAll(articles);
    }

    public void createProductIndex(final ArticleElastic article) {
        articleElasticRepo.save(article);
    }

    public Iterable<ArticleElastic> findAll() {
        return articleElasticRepo.findAll();
    }
}
