package com.ipmugo.library.elastic.data;

import java.util.Date;
import java.util.List;

import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.data.elasticsearch.annotations.MultiField;

import com.ipmugo.library.data.Category;
import com.ipmugo.library.data.Metric;
import com.ipmugo.library.utils.Frequency;

import jakarta.persistence.Id;

@Document(indexName = "articles")
public class JournalElastic {

    @Id
    @Field(type = FieldType.Keyword)
    private String id;

    @Field(type = FieldType.Keyword)
    private String name;

    @Field(type = FieldType.Keyword)
    private String issn;

    @Field(type = FieldType.Keyword)
    private String e_issn;

    @Field(type = FieldType.Keyword)
    private String abbreviation;

    @Field(type = FieldType.Keyword)
    private String thumbnail;

    @MultiField(mainField = @Field(type = FieldType.Text, fielddata = true))
    private String description;

    @Field(type = FieldType.Keyword)
    private String publisher;

    @Field(type = FieldType.Keyword)
    private String journal_site;

    @Field(type = FieldType.Keyword)
    private Frequency frequency;

    @Field(type = FieldType.Keyword)
    private String country;

    @Field(type = FieldType.Keyword)
    private String aim_scope_site;

    @Field(type = FieldType.Keyword)
    private String introduction_author_site;

    @Field(type = FieldType.Keyword)
    private String host_platform = "ojs";

    @Field(type = FieldType.Keyword)
    private Integer issue_per_year;

    @Field(type = FieldType.Keyword)
    private String primary_languange = "en";

    @Field(type = FieldType.Keyword)
    private String editor_site;

    @Field(type = FieldType.Keyword)
    private String full_text_format = "application/pdf";

    @Field(type = FieldType.Keyword)
    private boolean article_doi = true;

    @Field(type = FieldType.Keyword)
    private String statement;

    @Field(type = FieldType.Keyword)
    private String license;

    @Field(type = FieldType.Keyword)
    private Double apc_fee;

    @Field(type = FieldType.Keyword)
    private String review_police;

    @Field(type = FieldType.Nested, includeInParent = true)
    private List<Category> categories;

    @Field(type = FieldType.Object, includeInParent = true)
    private Metric metric;

    @Field(type = FieldType.Date)
    private Date updatedAt;

    @Field(type = FieldType.Date)
    private Date createdAt;

    public String getId() {
        return id;
    }

    public void setId(String id) {
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

    public List<Category> getCategories() {
        return categories;
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }

    public Metric getMetric() {
        return metric;
    }

    public void setMetric(Metric metric) {
        this.metric = metric;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

}
