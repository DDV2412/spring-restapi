package com.ipmugo.library.elastic.repository;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import com.ipmugo.library.elastic.data.ArticleElastic;

public interface ArticleElasticRepo extends ElasticsearchRepository<ArticleElastic, String> {

}
