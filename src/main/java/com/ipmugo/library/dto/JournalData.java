package com.ipmugo.library.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public class JournalData {

    @NotEmpty(message = "Journal name cannot be an empty")
    private String name;

    @NotEmpty(message = "ISSN cannot be an empty field")
    private String issn;

    @NotEmpty(message = "E-ISSN cannot be an empty field")
    private String e_issn;

    @NotEmpty(message = "Abbreviation cannot be an empty field")
    private String abbreviation;

    @NotEmpty(message = "Journal thumbnail cannot be an empty field")
    private String thumbnail;

    @NotEmpty(message = "Journal description cannot be an empty field")
    private String description;

    @NotEmpty(message = "Publisher cannot be an empty field")
    private String publisher;

    @NotEmpty(message = "Journal site cannot be an empty field")
    private String journal_site;

    @NotEmpty(message = "Journal frequency cannot be an empty field")
    private String frequency;

    @NotEmpty(message = "Country cannot be an empty field")
    private String country;

    @NotEmpty(message = "Journal aim scope cannot be an empty field")
    private String aim_scope_site;

    @NotEmpty(message = "Journal introduction author cannot be an empty field")
    private String introduction_author_site;

    private String host_platform;

    @NotNull(message = "Issue per year is required field")
    private Integer issue_per_year;

    private String primary_languange;

    @NotEmpty(message = "Editor site cannot be an empty field")
    private String editor_site;

    private String full_text_format;

    private boolean article_doi;

    @NotEmpty(message = "Statement cannot be an empty field")
    private String statement;

    private String license;

    private Double apc_fee;

    private String review_police;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIssn() {
        return issn;
    }

    public void setIssn(String issn) {
        this.issn = issn;
    }

    public String getE_issn() {
        return e_issn;
    }

    public void setE_issn(String e_issn) {
        this.e_issn = e_issn;
    }

    public String getAbbreviation() {
        return abbreviation;
    }

    public void setAbbreviation(String abbreviation) {
        this.abbreviation = abbreviation;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getJournal_site() {
        return journal_site;
    }

    public void setJournal_site(String journal_site) {
        this.journal_site = journal_site;
    }

    public String getFrequency() {
        return frequency;
    }

    public void setFrequency(String frequency) {
        this.frequency = frequency;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getAim_scope_site() {
        return aim_scope_site;
    }

    public void setAim_scope_site(String aim_scope_site) {
        this.aim_scope_site = aim_scope_site;
    }

    public String getIntroduction_author_site() {
        return introduction_author_site;
    }

    public void setIntroduction_author_site(String introduction_author_site) {
        this.introduction_author_site = introduction_author_site;
    }

    public String getHost_platform() {
        return host_platform;
    }

    public void setHost_platform(String host_platform) {
        this.host_platform = host_platform;
    }

    public Integer getIssue_per_year() {
        return issue_per_year;
    }

    public void setIssue_per_year(Integer issue_per_year) {
        this.issue_per_year = issue_per_year;
    }

    public String getPrimary_languange() {
        return primary_languange;
    }

    public void setPrimary_languange(String primary_languange) {
        this.primary_languange = primary_languange;
    }

    public String getEditor_site() {
        return editor_site;
    }

    public void setEditor_site(String editor_site) {
        this.editor_site = editor_site;
    }

    public String getFull_text_format() {
        return full_text_format;
    }

    public void setFull_text_format(String full_text_format) {
        this.full_text_format = full_text_format;
    }

    public boolean isArticle_doi() {
        return article_doi;
    }

    public void setArticle_doi(boolean article_doi) {
        this.article_doi = article_doi;
    }

    public String getStatement() {
        return statement;
    }

    public void setStatement(String statement) {
        this.statement = statement;
    }

    public String getLicense() {
        return license;
    }

    public void setLicense(String license) {
        this.license = license;
    }

    public Double getApc_fee() {
        return apc_fee;
    }

    public void setApc_fee(Double apc_fee) {
        this.apc_fee = apc_fee;
    }

    public String getReview_police() {
        return review_police;
    }

    public void setReview_police(String review_police) {
        this.review_police = review_police;
    }

}
