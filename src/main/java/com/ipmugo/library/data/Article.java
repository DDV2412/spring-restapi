package com.ipmugo.library.data;

import java.time.Year;
import java.util.Date;
import java.util.UUID;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

@Entity
public class Article {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    private Journal journal;

    @ManyToMany
    private Subject subject;

    private String title;

    private String pages;

    private String publisher;

    private Year publish_year;

    private Date publish_date;

    private String issn;

    private String source_type;

    private String languange_publication;

    private String doi;

    private Integer volume;

    private Integer issue;

    private String copyright;

    private String abstract_text;

    private String full_text;

    @Temporal(TemporalType.TIMESTAMP)
    private Date updated_at;

    @Temporal(TemporalType.TIMESTAMP)
    private Date created_at;

    @OneToMany
    private Author authors;

    @OneToMany
    private Figure figures;

    public Article() {
    }

    public Article(UUID id, Journal journal, Subject subject, String title, String pages, String publisher,
            Year publish_year, Date publish_date, String issn, String source_type, String languange_publication,
            String doi, Integer volume, Integer issue, String copyright, String abstract_text, String full_text,
            Date updated_at, Date created_at, Author authors, Figure figures) {
        this.id = id;
        this.journal = journal;
        this.subject = subject;
        this.title = title;
        this.pages = pages;
        this.publisher = publisher;
        this.publish_year = publish_year;
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
        this.updated_at = updated_at;
        this.created_at = created_at;
        this.authors = authors;
        this.figures = figures;
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

    public Subject getSubject() {
        return subject;
    }

    public void setSubject(Subject subject) {
        this.subject = subject;
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

    public Year getPublish_year() {
        return publish_year;
    }

    public void setPublish_year(Year publish_year) {
        this.publish_year = publish_year;
    }

    public Date getPublish_date() {
        return publish_date;
    }

    public void setPublish_date(Date publish_date) {
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

    public Integer getVolume() {
        return volume;
    }

    public void setVolume(Integer volume) {
        this.volume = volume;
    }

    public Integer getIssue() {
        return issue;
    }

    public void setIssue(Integer issue) {
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

    public Author getAuthors() {
        return authors;
    }

    public void setAuthors(Author authors) {
        this.authors = authors;
    }

    public Figure getFigures() {
        return figures;
    }

    public void setFigures(Figure figures) {
        this.figures = figures;
    }

}
