package com.ipmugo.library.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ipmugo.library.data.Article;
import com.ipmugo.library.repository.ArticleRepo;

import jakarta.transaction.TransactionScoped;

@Service
@TransactionScoped
public class ArticleService {
    @Autowired
    private ArticleRepo articleRepo;

    public List<Article> findAll() {
        return articleRepo.findAll();
    }

    public Article findOne(UUID id) {
        Optional<Article> article = articleRepo.findById(id);

        if (!article.isPresent()) {
            return null;
        }

        return article.get();
    }

    public Article save(Article article) {
        return articleRepo.save(article);
    }

    public void deleteById(UUID id) {
        articleRepo.deleteById(id);
    }

    public Article findByDoi(String doi) {
        Optional<Article> article = articleRepo.findByDoi(doi);

        if (!article.isPresent()) {
            return null;
        }

        return article.get();
    }

    public Article findByTitle(String title) {
        Optional<Article> article = articleRepo.findByTitle(title);

        if (!article.isPresent()) {
            return null;
        }

        return article.get();
    }

}
