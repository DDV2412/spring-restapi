package com.ipmugo.library.data;

import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "author")
public class Author {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, length = 255)
    private String firstName;

    @Column(nullable = false, length = 255)
    private String lastName;

    @Column(nullable = true, length = 64)
    private String email;

    @Column(nullable = true, length = 255)
    private String orcid;

    @Column(nullable = true, length = 255)
    private String scopus_id;

    @Column(nullable = true, length = 255)
    private String google_scholar;

    @Column(nullable = true, length = 255)
    private String affiliation;

    @Column(nullable = true, length = 255)
    private String photo_profile;

    @ManyToOne
    @JoinColumn(name = "article_id", nullable = false)
    @JsonBackReference
    private Article article;

    @OneToMany(mappedBy = "author", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<COAuthor> co_author;

    @OneToMany(mappedBy = "author", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<AuthorCitation> author_citation;

    @OneToOne(mappedBy = "author", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private AuthorStatistic author_statistic;

    public Author() {
    }

    public Author(UUID id, String firstName, String lastName, String email, String orcid, String scopus_id,
            String google_scholar, String affiliation, String photo_profile, Article article) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.orcid = orcid;
        this.scopus_id = scopus_id;
        this.google_scholar = google_scholar;
        this.affiliation = affiliation;
        this.photo_profile = photo_profile;
        this.article = article;
    }

    public List<COAuthor> getCo_author() {
        return co_author;
    }

    public void setCo_author(List<COAuthor> co_author) {
        this.co_author = co_author;
    }

    public List<AuthorCitation> getAuthor_citation() {
        return author_citation;
    }

    public void setAuthor_citation(List<AuthorCitation> author_citation) {
        this.author_citation = author_citation;
    }

    public AuthorStatistic getAuthor_statistic() {
        return author_statistic;
    }

    public void setAuthor_statistic(AuthorStatistic author_statistic) {
        this.author_statistic = author_statistic;
    }

    public String getPhoto_profile() {
        return photo_profile;
    }

    public void setPhoto_profile(String photo_profile) {
        this.photo_profile = photo_profile;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
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
