package com.ipmugo.library.elastic.data;

import java.util.UUID;

import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import jakarta.persistence.Id;

@Document(indexName = "articles")
public class AuthorElastic {

    @Id
    @Field(type = FieldType.Keyword)
    private UUID id;

    @Field(type = FieldType.Text)
    private String first_name;

    @Field(type = FieldType.Text)
    private String last_name;

    @Field(type = FieldType.Keyword)
    private String email;

    @Field(type = FieldType.Keyword)
    private String orcid;

    @Field(type = FieldType.Keyword)
    private String scopus_id;

    @Field(type = FieldType.Keyword)
    private String google_scholar;

    @Field(type = FieldType.Text)
    private String affiliation;

    @Field(type = FieldType.Keyword)
    private String photo_profile;

    @Field(type = FieldType.Object, includeInParent = true)
    private COAuthorElastic co_author;

    @Field(type = FieldType.Object, includeInParent = true)
    private AuthorCitationElastic author_citation;

    @Field(type = FieldType.Object, includeInParent = true)
    private AuthorStatisticElastic author_statistic;

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

    public String getPhoto_profile() {
        return photo_profile;
    }

    public void setPhoto_profile(String photo_profile) {
        this.photo_profile = photo_profile;
    }

    public COAuthorElastic getCo_author() {
        return co_author;
    }

    public void setCo_author(COAuthorElastic co_author) {
        this.co_author = co_author;
    }

    public AuthorCitationElastic getAuthor_citation() {
        return author_citation;
    }

    public void setAuthor_citation(AuthorCitationElastic author_citation) {
        this.author_citation = author_citation;
    }

    public AuthorStatisticElastic getAuthor_statistic() {
        return author_statistic;
    }

    public void setAuthor_statistic(AuthorStatisticElastic author_statistic) {
        this.author_statistic = author_statistic;
    }

}
