package com.ipmugo.library.data;

import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class Author {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, length = 100)
    private String first_name;

    @Column(nullable = false, length = 100)
    private String last_name;

    @Column(nullable = true, length = 100)
    private String email;

    @Column(nullable = true, length = 100)
    private String orcid;

    @Column(nullable = true, length = 100)
    private String scopus_id;

    @Column(nullable = true, length = 100)
    private String google_scholar;

    @Column(nullable = true, length = 255)
    private String affiliation;

    @ManyToOne
    @JoinColumn(name = "article_id", nullable = false)
    private Article article;

    public Author() {
    }

    public Author(UUID id, String first_name, String last_name, String email, String orcid, String scopus_id,
            String google_scholar, String affiliation, Article article) {
        this.id = id;
        this.first_name = first_name;
        this.last_name = last_name;
        this.email = email;
        this.orcid = orcid;
        this.scopus_id = scopus_id;
        this.google_scholar = google_scholar;
        this.affiliation = affiliation;
        this.article = article;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getOrcid() {
        return orcid;
    }

    public void setOrcid(String orcid) {
        this.orcid = orcid;
    }

    public String getScopus_id() {
        return scopus_id;
    }

    public void setScopus_id(String scopus_id) {
        this.scopus_id = scopus_id;
    }

    public String getGoogle_scholar() {
        return google_scholar;
    }

    public void setGoogle_scholar(String google_scholar) {
        this.google_scholar = google_scholar;
    }

    public String getAffiliation() {
        return affiliation;
    }

    public void setAffiliation(String affiliation) {
        this.affiliation = affiliation;
    }

    public Article getArticle() {
        return article;
    }

    public void setArticle(Article article) {
        this.article = article;
    }

}
