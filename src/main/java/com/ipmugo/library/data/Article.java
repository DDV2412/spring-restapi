package com.ipmugo.library.data;

import java.util.Date;
import java.util.Set;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

@Entity
public class Article {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "journal_id")
    private Journal journal;

    @Column(nullable = true)
    private Integer ojs_id;

    @Column(nullable = false, length = 100)
    private String set_spec;

    @ManyToMany
    @JoinTable(name = "subject_article", joinColumns = @JoinColumn(name = "article_id"), inverseJoinColumns = @JoinColumn(name = "subject_id"))
    @JsonManagedReference
    private Set<Subject> subjects;

    @Column(nullable = true, length = 255)
    private String figure;

    @Column(length = 255, nullable = false)
    private String title;

    @Column(length = 10, nullable = false)
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

    @Column(length = 3, nullable = false)
    private String languange_publication = "en";

    @Column(length = 255, nullable = false, unique = true)
    private String doi;

    @Column(nullable = false)
    private String volume;

    @Column(nullable = false)
    private String issue;

    @Column(length = 255, nullable = true)
    private String copyright;

    @Column(length = 10000, nullable = false)
    private String abstract_text;

    @Column(length = 10000, nullable = true)
    private String full_text;

    @Column(length = 255, nullable = true)
    private String article_pdf;

    @Column(length = 255, nullable = true)
    private String keyword;

    @Temporal(TemporalType.TIMESTAMP)
    private Date updated_at;

    @Temporal(TemporalType.TIMESTAMP)
    private Date created_at;

    public Article() {
    }

    public Article(UUID id, Journal journal, Integer ojs_id, String set_spec, Set<Subject> subjects, String figure,
            String title, String pages, String publisher, String publish_year, String last_modifier,
            String publish_date, String issn, String source_type, String languange_publication, String doi,
            String volume, String issue, String copyright, String abstract_text, String full_text, String article_pdf,
            String keyword, Date updated_at, Date created_at) {
        this.id = id;
        this.journal = journal;
        this.ojs_id = ojs_id;
        this.set_spec = set_spec;
        this.subjects = subjects;
        this.figure = figure;
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
        this.updated_at = updated_at;
        this.created_at = created_at;
    }

    public UUID getId() {
        return id;
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

    public String getFigure() {
        return figure;
    }

    public void setFigure(String figure) {
        this.figure = figure;
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

    public Date getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(Date updated_at) {
        this.updated_at = updated_at;
    }

    public Date getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Date created_at) {
        this.created_at = created_at;
    }

}
