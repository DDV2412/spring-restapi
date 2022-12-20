package com.ipmugo.library.controllers;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.jbibtex.BibTeXDatabase;
import org.jbibtex.BibTeXEntry;
import org.jbibtex.BibTeXFormatter;
import org.jbibtex.Key;
import org.jbibtex.StringValue;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.net.HttpHeaders;
import com.ipmugo.library.data.Article;
import com.ipmugo.library.data.Author;
import com.ipmugo.library.data.CitationCrossRef;
import com.ipmugo.library.data.CitationScopus;
import com.ipmugo.library.data.Journal;
import com.ipmugo.library.dto.ArticleData;
import com.ipmugo.library.dto.ResponseData;
import com.ipmugo.library.dto.ResponseDataWithCount;
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
    public ResponseEntity<ResponseDataWithCount<Iterable<Article>>> findAll(@RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "15") int size) {
        ResponseDataWithCount<Iterable<Article>> responseData = new ResponseDataWithCount<>();

        Pageable pageable = PageRequest.of(page, size);

        Page<Article> article = articleService.findAll(pageable);

        responseData.setStatus(true);
        responseData.setPayload(article.getContent());
        responseData.setTotalElements(article.getTotalElements());
        responseData.setTotalPage(article.getTotalPages());
        responseData.setCurrentPage(article.getNumber());
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

    @GetMapping("/doi/{doi}")
    public ResponseEntity<ResponseData<Article>> findByDoi(@PathVariable("doi") String doi) {
        ResponseData<Article> responseData = new ResponseData<>();

        doi = doi.replace("+", "/");
        Article article = articleService.findByDoi(doi);

        if (article == null) {
            responseData.setStatus(false);
            responseData.getMessages().add("Article with " + doi + " not found");

            return ResponseEntity.badRequest().body(responseData);
        }

        responseData.setPayload(article);
        responseData.setStatus(true);
        responseData.getMessages().add("Successfully get article by ID " + doi);

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
    public ResponseEntity<ResponseData<CitationScopus>> setCitationScopus(@PathVariable("id") UUID id) {
        ResponseData<CitationScopus> responseData = new ResponseData<>();

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
    public ResponseEntity<ResponseData<CitationCrossRef>> setCitationCrossRef(@PathVariable("id") UUID id) {
        ResponseData<CitationCrossRef> responseData = new ResponseData<>();

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

    @PostMapping("/export/citation")
    public ResponseEntity<byte[]> exportMultiple(@RequestBody List<UUID> id) {

        try {

            Iterable<Article> articles = articleService.findById(id);

            List<String> exportBib = new ArrayList<>();

            for (Article article : articles) {
                BibTeXDatabase database = new BibTeXDatabase();
                BibTeXEntry entry = new BibTeXEntry(BibTeXEntry.TYPE_ARTICLE,
                        new Key(article.getAuthors().get(0).getFirst_name() + "@" + article.getPublish_year()));
                entry.addField(BibTeXEntry.KEY_TITLE, new StringValue(article.getTitle(), StringValue.Style.BRACED));
                entry.addField(BibTeXEntry.KEY_JOURNAL,
                        new StringValue(article.getJournal().getName(), StringValue.Style.BRACED));
                entry.addField(BibTeXEntry.KEY_YEAR,
                        new StringValue(article.getPublish_year(), StringValue.Style.BRACED));
                entry.addField(BibTeXEntry.KEY_VOLUME, new StringValue(article.getVolume(), StringValue.Style.BRACED));
                entry.addField(BibTeXEntry.KEY_DOI, new StringValue(article.getDoi(), StringValue.Style.BRACED));
                entry.addField(BibTeXEntry.KEY_PAGES, new StringValue(article.getPages(), StringValue.Style.BRACED));
                entry.addField(BibTeXEntry.KEY_NUMBER, new StringValue(article.getIssue(), StringValue.Style.BRACED));

                StringBuilder keyAuthorBuilder = new StringBuilder();
                for (int i = 0; i < article.getAuthors().size(); i++) {
                    Author author = article.getAuthors().get(i);
                    keyAuthorBuilder.append(author.getFirst_name()).append(" ").append(author.getLast_name());
                    if (i < article.getAuthors().size() - 1) {
                        keyAuthorBuilder.append(" and ");
                    }
                }

                entry.addField(BibTeXEntry.KEY_AUTHOR,
                        new StringValue(keyAuthorBuilder.toString(), StringValue.Style.BRACED));
                database.addObject(entry);

                BibTeXFormatter formatter = new BibTeXFormatter();
                StringWriter stringWriter = new StringWriter();
                formatter.format(database, stringWriter);
                String bibtexString = stringWriter.toString();

                exportBib.add(bibtexString);
            }

            return ResponseEntity.ok().contentType(MediaType.valueOf("application/x-bibtex"))
                    .contentLength(String.join("\n",
                            exportBib).length())
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"citation.bib")
                    .body(String.join("\n",
                            exportBib).getBytes());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/export/citation/{id}")
    public ResponseEntity<byte[]> exportSingle(@PathVariable("id") UUID id) {
        try {

            Article article = articleService.findOne(id);

            BibTeXDatabase database = new BibTeXDatabase();
            BibTeXEntry entry = new BibTeXEntry(BibTeXEntry.TYPE_ARTICLE,
                    new Key(article.getAuthors().get(0).getFirst_name() + "@" + article.getPublish_year()));
            entry.addField(BibTeXEntry.KEY_TITLE, new StringValue(article.getTitle(), StringValue.Style.BRACED));
            entry.addField(BibTeXEntry.KEY_JOURNAL,
                    new StringValue(article.getJournal().getName(), StringValue.Style.BRACED));
            entry.addField(BibTeXEntry.KEY_YEAR, new StringValue(article.getPublish_year(), StringValue.Style.BRACED));
            entry.addField(BibTeXEntry.KEY_VOLUME, new StringValue(article.getVolume(), StringValue.Style.BRACED));
            entry.addField(BibTeXEntry.KEY_DOI, new StringValue(article.getDoi(), StringValue.Style.BRACED));
            entry.addField(BibTeXEntry.KEY_PAGES, new StringValue(article.getPages(), StringValue.Style.BRACED));
            entry.addField(BibTeXEntry.KEY_NUMBER, new StringValue(article.getIssue(), StringValue.Style.BRACED));
            StringBuilder keyAuthorBuilder = new StringBuilder();
            for (int i = 0; i < article.getAuthors().size(); i++) {
                Author author = article.getAuthors().get(i);
                keyAuthorBuilder.append(author.getFirst_name()).append(" ").append(author.getLast_name());
                if (i < article.getAuthors().size() - 1) {
                    keyAuthorBuilder.append(" and ");
                }
            }

            entry.addField(BibTeXEntry.KEY_AUTHOR,
                    new StringValue(keyAuthorBuilder.toString(), StringValue.Style.BRACED));
            database.addObject(entry);

            BibTeXFormatter formatter = new BibTeXFormatter();
            StringWriter stringWriter = new StringWriter();
            formatter.format(database, stringWriter);
            String bibtexString = stringWriter.toString();

            return ResponseEntity.ok().contentType(MediaType.valueOf("application/x-bibtex"))
                    .contentLength(bibtexString.length())
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"citation.bib")
                    .body(bibtexString.getBytes());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

}
