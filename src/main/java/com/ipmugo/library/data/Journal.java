package com.ipmugo.library.data;

import java.util.Date;
import java.util.Set;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.ipmugo.library.utils.Frequency;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

@Entity
@Table(name = "journal")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Journal {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(length = 255, nullable = false)
    private String name;

    @Column(length = 12, nullable = false, unique = true)
    private String issn;

    @Column(length = 12, nullable = false, unique = true)
    private String e_issn;

    @Column(length = 12, nullable = false, unique = true)
    private String abbreviation;

    @Column(length = 1000, nullable = false)
    private String thumbnail;

    @Column(length = 10000, nullable = false)
    private String description;

    @Column(length = 255, nullable = false)
    private String publisher;

    @Column(length = 255, nullable = false, unique = true)
    private String journal_site;

    @Column(length = 100, nullable = false)
    @Enumerated
    private Frequency frequency;

    @Column(length = 100, nullable = false)
    private String country;

    @Column(length = 255, nullable = false)
    private String aim_scope_site;

    @Column(length = 255, nullable = false)
    private String introduction_author_site;

    @Column(length = 100, nullable = false)
    private String host_platform = "ojs";

    @Column(nullable = false)
    private Integer issue_per_year;

    @Column(length = 3, nullable = false)
    private String primary_languange = "en";

    @Column(length = 255, nullable = true)
    private String editor_site;

    @Column(length = 48, nullable = false)
    private String full_text_format = "application/pdf";

    @Column(nullable = false)
    private boolean article_doi = true;

    @Column(length = 255, nullable = false)
    private String statement;

    @Column(length = 255, nullable = true)
    private String license;

    @Column(nullable = true)
    private Double apc_fee;

    @Column(length = 255, nullable = true)
    private String review_police;

    @Temporal(TemporalType.TIMESTAMP)
    private Date updated_at;

    @Temporal(TemporalType.TIMESTAMP)
    private Date created_at;

    @ManyToMany(mappedBy = "journals", fetch = FetchType.EAGER)
    private Set<Category> categories;

    @OneToOne(mappedBy = "journal", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Metric metric;

    public Journal() {
    }

    public Journal(UUID id, String name, String issn, String e_issn, String abbreviation, String thumbnail,
            String description, String publisher, String journal_site, Frequency frequency, String country,
            String aim_scope_site, String introduction_author_site, String host_platform, Integer issue_per_year,
            String primary_languange, String editor_site, String full_text_format, boolean article_doi,
            String statement, String license, Double apc_fee, String review_police, Date updated_at, Date created_at,
            Metric metric) {
        this.id = id;
        this.name = name;
        this.issn = issn;
        this.e_issn = e_issn;
        this.abbreviation = abbreviation;
        this.thumbnail = thumbnail;
        this.description = description;
        this.publisher = publisher;
        this.journal_site = journal_site;
        this.frequency = frequency;
        this.country = country;
        this.aim_scope_site = aim_scope_site;
        this.introduction_author_site = introduction_author_site;
        this.host_platform = host_platform;
        this.issue_per_year = issue_per_year;
        this.primary_languange = primary_languange;
        this.editor_site = editor_site;
        this.full_text_format = full_text_format;
        this.article_doi = article_doi;
        this.statement = statement;
        this.license = license;
        this.apc_fee = apc_fee;
        this.review_police = review_police;
        this.updated_at = updated_at;
        this.created_at = created_at;
        this.metric = metric;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

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

    public Frequency getFrequency() {
        return frequency;
    }

    public void setFrequency(Frequency frequency) {
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

    public Set<Category> getCategories() {
        return categories;
    }

    public void setCategories(Set<Category> categories) {
        this.categories = categories;
    }

    public Metric getMetric() {
        return metric;
    }

    public void setMetric(Metric metric) {
        this.metric = metric;
    }

}
