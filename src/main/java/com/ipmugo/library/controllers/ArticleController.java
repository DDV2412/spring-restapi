package com.ipmugo.library.controllers;

import java.util.UUID;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ipmugo.library.data.Article;
import com.ipmugo.library.data.Journal;
import com.ipmugo.library.dto.ArticleData;
import com.ipmugo.library.dto.ResponseData;
import com.ipmugo.library.service.ArticleService;
import com.ipmugo.library.service.JournalService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/article")
public class ArticleController {

    @Autowired
    private ArticleService articleService;

    @Autowired
    private JournalService journalService;

    @Autowired
    private ModelMapper modelMapper;

    @GetMapping
    public ResponseEntity<ResponseData<Iterable<Article>>> findAll() {
        ResponseData<Iterable<Article>> responseData = new ResponseData<>();

        Iterable<Article> article = articleService.findAll();

        responseData.setStatus(true);
        responseData.setPayload(article);
        return ResponseEntity.ok(responseData);

    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseData<Article>> findOne(@PathVariable("id") UUID id) {
        ResponseData<Article> responseData = new ResponseData<>();

        Article article = articleService.findOne(id);

        if (article == null) {
            responseData.setStatus(false);
            responseData.getMessages().add("Article with " + id + " not found");

            return ResponseEntity.badRequest().body(responseData);
        }

        responseData.setPayload(article);
        responseData.setStatus(true);
        responseData.getMessages().add("Successfully get article by ID " + id);

        return ResponseEntity.ok(responseData);

    }

    @PostMapping
    public ResponseEntity<ResponseData<Article>> create(@Valid @RequestBody ArticleData articleData, Errors errors) {
        ResponseData<Article> responseData = new ResponseData<>();

        if (errors.hasErrors()) {
            for (ObjectError error : errors.getAllErrors()) {
                responseData.getMessages().add(error.getDefaultMessage());
            }
            responseData.setStatus(false);
            responseData.setPayload(null);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseData);
        }
        try {
            Article article = modelMapper.map(articleData, Article.class);

            Journal journal = journalService.findOne(UUID.fromString(articleData.getJournal_id()));

            if (journal == null) {
                responseData.setStatus(false);
                responseData.getMessages().add("Journal with " + articleData.getJournal_id() + " not found");

                return ResponseEntity.badRequest().body(responseData);
            }

            article.setJournal(journal);

            responseData.setStatus(true);
            responseData.setPayload(articleService.save(article));
            return ResponseEntity.ok(responseData);
        } catch (Exception e) {
            responseData.setStatus(false);
            responseData.getMessages().add(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseData);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseData<Article>> update(@PathVariable("id") UUID id,
            @Valid @RequestBody ArticleData articleData, Errors errors) {
        ResponseData<Article> responseData = new ResponseData<>();

        if (errors.hasErrors()) {
            for (ObjectError error : errors.getAllErrors()) {
                responseData.getMessages().add(error.getDefaultMessage());
            }
            responseData.setStatus(false);
            responseData.setPayload(null);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseData);
        }
        try {
            Article article = articleService.findOne(id);

            Journal journal = journalService.findOne(UUID.fromString(articleData.getJournal_id()));

            if (journal == null) {
                responseData.setStatus(false);
                responseData.getMessages().add("Journal with " + articleData.getJournal_id() + " not found");

                return ResponseEntity.badRequest().body(responseData);
            }

            if (article == null) {
                responseData.setStatus(false);
                responseData.getMessages().add("Article with " + id + " not found");

                return ResponseEntity.badRequest().body(responseData);
            }

            Article articleMapper = modelMapper.map(articleData, Article.class);

            article.setJournal(journal);
            article.setOjs_id(articleMapper.getOjs_id());
            article.setSet_spec(articleMapper.getSet_spec());
            article.setFigure(articleMapper.getFigure());
            article.setTitle(articleMapper.getTitle());
            article.setPages(articleMapper.getPages());
            article.setPublisher(articleMapper.getPublisher());
            article.setPublish_date(articleMapper.getPublish_date());
            article.setLast_modifier(articleMapper.getLast_modifier());
            article.setPublish_year(articleMapper.getPublish_year());
            article.setIssn(articleMapper.getIssn());
            article.setSource_type(articleMapper.getSource_type());
            article.setLanguange_publication(articleMapper.getLanguange_publication());
            article.setDoi(articleMapper.getDoi());
            article.setIssue(articleMapper.getIssue());
            article.setVolume(articleMapper.getVolume());
            article.setCopyright(articleMapper.getCopyright());
            article.setAbstract_text(articleMapper.getAbstract_text());
            article.setFull_text(article.getFull_text());
            article.setArticle_pdf(articleMapper.getArticle_pdf());
            article.setKeyword(articleMapper.getKeyword());

            responseData.setStatus(true);
            responseData.setPayload(articleService.save(article));
            return ResponseEntity.ok(responseData);
        } catch (Exception e) {
            responseData.setStatus(false);
            responseData.getMessages().add("Article name " + articleData.getTitle() + " not available");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseData);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseData<Article>> deleteById(@PathVariable("id") UUID id) {
        ResponseData<Article> responseData = new ResponseData<>();

        try {
            Article article = articleService.findOne(id);

            if (article == null) {
                responseData.setStatus(false);
                responseData.getMessages().add("Article with " + id + " not found");

                return ResponseEntity.badRequest().body(responseData);
            }

            articleService.deleteById(id);

            responseData.setStatus(true);
            responseData.getMessages().add("Successfully delete article by ID" + id);

            return ResponseEntity.ok(responseData);
        } catch (Exception e) {
            responseData.setStatus(false);
            responseData.getMessages().add("Article by ID " + id + " not exist");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseData);
        }

    }

    @GetMapping("/citation-scopus/{id}")
    public ResponseEntity<ResponseData<Object>> setCitationScopus(@PathVariable("id") UUID id) {
        ResponseData<Object> responseData = new ResponseData<>();

        try {
            responseData.setStatus(true);
            responseData.getMessages().add("Successfully set scopus citation by ID" + id);
            responseData.setPayload(articleService.citationScopus(id));

            return ResponseEntity.ok(responseData);
        } catch (Exception e) {
            responseData.setStatus(false);
            responseData.getMessages().add("Article by ID " + id + " not exist");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseData);
        }
    }

    @GetMapping("/citation-crossref/{id}")
    public ResponseEntity<ResponseData<Object>> setCitationCrossRef(@PathVariable("id") UUID id) {
        ResponseData<Object> responseData = new ResponseData<>();

        try {
            responseData.setStatus(true);
            responseData.getMessages().add("Successfully set crossref citation by ID" + id);
            responseData.setPayload(articleService.citationCrossReff(id));

            return ResponseEntity.ok(responseData);
        } catch (Exception e) {
            responseData.setStatus(false);
            responseData.getMessages().add("Article by ID " + id + " not exist");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseData);
        }
    }

}
