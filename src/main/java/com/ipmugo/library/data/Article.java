package com.ipmugo.library.data;

import java.sql.Timestamp;
import java.util.Date;
import java.util.Set;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "article")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Article {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "journal_id")
    private Journal journal;

    @Column(length = 255, nullable = true)
    private String thumbnail;

    @Column(nullable = true)
    private Integer ojs_id;

    @Column(nullable = false, length = 100)
    private String set_spec;

    @ManyToMany(mappedBy = "articles", fetch = FetchType.EAGER)
    private Set<Subject> subjects;

    @Column(length = 255, nullable = false)
    private String title;

    @Column(length = 10, nullable = true)
    private String pages;

    @Column(length = 150, nullable = false)
    private String publisher;

    @Column(nullable = false)
    private String publish_year;

    @Column(nullable = false)
    private String last_modifier;

    @Column(nullable = false)
    private String publish_date;

    @Column(length = 12, nullable = false)
    private String issn;

    @Column(length = 255, nullable = true)
    private String source_type;

    @Column(length = 255, nullable = false)
    private String languange_publication = "en";

    @Column(length = 255, nullable = false, unique = true)
    private String doi;

    @Column(nullable = true)
    private String volume;

    @Column(nullable = true)
    private String issue;

    @Column(length = 255, nullable = true)
    private String copyright;

    @Column(columnDefinition = "TEXT")
    private String abstract_text;

    @Column(columnDefinition = "TEXT")
    private String full_text;

    @Column(length = 255, nullable = true)
    private String article_pdf;

    @Column(columnDefinition = "TEXT")
    private String keyword;

    @CreationTimestamp
    private Timestamp updatedAt;

    @CreationTimestamp
    private Timestamp createdAt;

    @OneToMany(mappedBy = "article", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Set<Author> authors;

    @OneToOne(mappedBy = "article", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private CitationScopus citation_by_scopus;

    @OneToOne(mappedBy = "article", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private CitationCrossRef citation_by_cross_ref;

    public Article() {
    }

    public Article(UUID id, Journal journal, String thumbnail, Integer ojs_id, String set_spec, Set<Subject> subjects,
            String title, String pages, String publisher, String publish_year, String last_modifier,
            String publish_date, String issn, String source_type, String languange_publication, String doi,
            String volume, String issue, String copyright, String abstract_text, String full_text, String article_pdf,
            String keyword, Timestamp updatedAt, Timestamp createdAt, Set<Author> authors,
            CitationScopus citation_by_scopus, CitationCrossRef citation_by_cross_ref) {
        this.id = id;
        this.journal = journal;
        this.thumbnail = thumbnail;
        this.ojs_id = ojs_id;
        this.set_spec = set_spec;
        this.subjects = subjects;
        this.title = title;
        this.pages = pages;
        this.publisher = publisher;
        this.publish_year = publish_year;
        this.last_modifier = last_modifier;
        this.publish_date = publish_date;
        this.issn = issn;
        this.source_type = source_type;
        this.languange_publication = languange_publication;
        this.doi = doi;
        this.volume = volume;
        this.issue = issue;
        this.copyright = copyright;
        this.abstract_text = abstract_text;
        this.full_text = full_text;
        this.article_pdf = article_pdf;
        this.keyword = keyword;
        this.updatedAt = updatedAt;
        this.createdAt = createdAt;
        this.authors = authors;
        this.citation_by_scopus = citation_by_scopus;
        this.citation_by_cross_ref = citation_by_cross_ref;
    }

    public UUID getId() {
        return id;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Journal getJournal() {
        return journal;
    }

    public void setJournal(Journal journal) {
        this.journal = journal;
    }

    public Integer getOjs_id() {
        return ojs_id;
    }

    public void setOjs_id(Integer ojs_id) {
        this.ojs_id = ojs_id;
    }

    public String getSet_spec() {
        return set_spec;
    }

    public void setSet_spec(String set_spec) {
        this.set_spec = set_spec;
    }

    public Set<Subject> getSubjects() {
        return subjects;
    }

    public void setSubjects(Set<Subject> subjects) {
        this.subjects = subjects;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPages() {
        return pages;
    }

    public void setPages(String pages) {
        this.pages = pages;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getPublish_year() {
        return publish_year;
    }

    public void setPublish_year(String publish_year) {
        this.publish_year = publish_year;
    }

    public String getLast_modifier() {
        return last_modifier;
    }

    public void setLast_modifier(String last_modifier) {
        this.last_modifier = last_modifier;
    }

    public String getPublish_date() {
        return publish_date;
    }

    public void setPublish_date(String publish_date) {
        this.publish_date = publish_date;
    }

    public String getIssn() {
        return issn;
    }

    public void setIssn(String issn) {
        this.issn = issn;
    }

    public String getSource_type() {
        return source_type;
    }

    public void setSource_type(String source_type) {
        this.source_type = source_type;
    }

    public String getLanguange_publication() {
        return languange_publication;
    }

    public void setLanguange_publication(String languange_publication) {
        this.languange_publication = languange_publication;
    }

    public String getDoi() {
        return doi;
    }

    public void setDoi(String doi) {
        this.doi = doi;
    }

    public String getVolume() {
        return volume;
    }

    public void setVolume(String volume) {
        this.volume = volume;
    }

    public String getIssue() {
        return issue;
    }

    public void setIssue(String issue) {
        this.issue = issue;
    }

    public String getCopyright() {
        return copyright;
    }

    public void setCopyright(String copyright) {
        this.copyright = copyright;
    }

    public String getAbstract_text() {
        return abstract_text;
    }

    public void setAbstract_text(String abstract_text) {
        this.abstract_text = abstract_text;
    }

    public String getFull_text() {
        return full_text;
    }

    public void setFull_text(String full_text) {
        this.full_text = full_text;
    }

    public String getArticle_pdf() {
        return article_pdf;
    }

    public void setArticle_pdf(String article_pdf) {
        this.article_pdf = article_pdf;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public Date getupdatedAt() {
        return updatedAt;
    }

    public void setupdatedAt(Timestamp updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Date getcreatedAt() {
        return createdAt;
    }

    public void setcreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public Set<Author> getAuthors() {
        return authors;
    }

    public void setAuthors(Set<Author> authors) {
        this.authors = authors;
    }

    public CitationScopus getCitation_by_scopus() {
        return citation_by_scopus;
    }

    public void setCitation_by_scopus(CitationScopus citation_by_scopus) {
        this.citation_by_scopus = citation_by_scopus;
    }

    public CitationCrossRef getCitation_by_cross_ref() {
        return citation_by_cross_ref;
    }

    public void setCitation_by_cross_ref(CitationCrossRef citation_by_cross_ref) {
        this.citation_by_cross_ref = citation_by_cross_ref;
    }

}
