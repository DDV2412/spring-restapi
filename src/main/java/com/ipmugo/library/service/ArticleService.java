package com.ipmugo.library.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.ipmugo.library.data.Article;
import com.ipmugo.library.data.CitationCrossRef;
import com.ipmugo.library.data.CitationScopus;
import com.ipmugo.library.dto.EntryCrossRef;
import com.ipmugo.library.dto.EntryScopus;
import com.ipmugo.library.dto.ExampleCitationCrossRef;
import com.ipmugo.library.dto.ExampleCitationScopus;
import com.ipmugo.library.dto.SearchResults;
import com.ipmugo.library.repository.ArticleRepo;
import com.ipmugo.library.repository.CitationCrossRefRepo;
import com.ipmugo.library.repository.CitationScopusRepo;

import jakarta.transaction.TransactionScoped;

@Service
@TransactionScoped
public class ArticleService {

    @Autowired
    private ArticleRepo articleRepo;

    @Autowired
    private CitationScopusRepo citationScopusRepo;

    @Autowired
    private CitationCrossRefRepo citationCrossRefRepo;

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

    public <T> CitationScopus citationScopus(UUID id) {
        try {
            Article article = findOne(id);

            if (article == null) {
                return null;
            }
            HttpHeaders headers = new HttpHeaders();
            headers.set("X-ELS-APIKey", "bb0f9584e36074a974a78c90396f08f5");

            HttpEntity<T> request = new HttpEntity<>(headers);

            ResponseEntity<ExampleCitationScopus> result = restTemplate.exchange(
                    "https://api.elsevier.com/content/search/scopus?query=DOI(" + article.getDoi() + ")",
                    HttpMethod.GET,
                    request,
                    new ParameterizedTypeReference<ExampleCitationScopus>() {
                    });

            if (result.getStatusCode().value() != 200) {
                return null;
            }

            ExampleCitationScopus data = result.getBody();

            if (data == null) {
                return null;
            }

            SearchResults searchResults = data.getSearchResults();

            if (searchResults.getEntry().isEmpty() && searchResults.getEntry().get(0).getCitedbyCount() == null) {
                return null;
            }

            List<EntryScopus> entryScopus = searchResults.getEntry();

            if (entryScopus.isEmpty()) {
                return null;
            }

            Optional<CitationScopus> citationScopus = citationScopusRepo.findByArticleId(article.getId());

            if (!citationScopus.isPresent()) {
                CitationScopus citationScopus2 = new CitationScopus();
                citationScopus2.setArticle(article);
                citationScopus2.setReferences_count(Integer.valueOf(entryScopus.get(0).getCitedbyCount()));

                return citationScopusRepo.save(citationScopus2);
            }

            citationScopus.get().setArticle(article);
            citationScopus.get().setReferences_count(Integer.valueOf(entryScopus.get(0).getCitedbyCount()));

            return citationScopusRepo.save(citationScopus.get());

        } catch (Exception e) {
            return null;
        }
    }

    public CitationCrossRef citationCrossReff(UUID id) {
        try {
            Article article = findOne(id);

            if (article == null) {
                return null;
            }

            ResponseEntity<ExampleCitationCrossRef> result = restTemplate.exchange(
                    "https://api.crossref.org/works/" + article.getDoi(),
                    HttpMethod.GET,
                    null,
                    ExampleCitationCrossRef.class);

            if (result.getStatusCode().value() != 200) {
                return null;
            }

            ExampleCitationCrossRef body = result.getBody();

            if (body == null) {
                return null;
            }

            EntryCrossRef entryCrossRef = body.getMessage();

            Optional<CitationCrossRef> citationCrossRef = citationCrossRefRepo.findByArticleId(article.getId());

            if (!citationCrossRef.isPresent()) {
                CitationCrossRef citationCrossRef2 = new CitationCrossRef();
                citationCrossRef2.setArticle(article);
                citationCrossRef2.setReferences_count(entryCrossRef.getReferencesCount());

                return citationCrossRefRepo.save(citationCrossRef2);
            }

            citationCrossRef.get().setArticle(article);
            citationCrossRef.get().setReferences_count(entryCrossRef.getReferencesCount());

            return citationCrossRefRepo.save(citationCrossRef.get());
        } catch (Exception e) {
            return null;
        }
    }

}
