package com.ipmugo.library.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.ipmugo.library.data.Article;
import com.ipmugo.library.repository.ArticleRepo;

import jakarta.transaction.TransactionScoped;

@Service
@TransactionScoped
public class ArticleService {

    @Autowired
    private ArticleRepo articleRepo;

    private RestTemplate restTemplate = new RestTemplate();

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

    public <T> Object citationScopus(UUID id) {
        try {
            Article article = findOne(id);

            if (article == null) {
                return null;
            }
            HttpHeaders headers = new HttpHeaders();
            headers.set("X-ELS-APIKey", "bb0f9584e36074a974a78c90396f08f5");

            HttpEntity<T> request = new HttpEntity<>(headers);

            ResponseEntity<Object> result = restTemplate.exchange(
                    "https://api.elsevier.com/content/search/scopus?query=DOI(" + article.getDoi() + ")",
                    HttpMethod.GET,
                    request,
                    Object.class);

            return result;

        } catch (Exception e) {
            return null;
        }
    }

    public <T> Object citationCrossReff(UUID id) {
        try {
            Article article = findOne(id);

            if (article == null) {
                return null;
            }
            Object result = restTemplate.getForObject(
                    "https://api.crossref.org/works/" + article.getDoi(),
                    Object.class);

            return result;

        } catch (Exception e) {
            return null;
        }
    }

}
