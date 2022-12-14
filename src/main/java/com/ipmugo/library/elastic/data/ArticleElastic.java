package com.ipmugo.library.elastic.data;

import java.util.Date;
import java.util.Set;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.data.elasticsearch.annotations.MultiField;

@Document(indexName = "articles")
public class ArticleElastic {

    @Id
    @Field(type = FieldType.Keyword)
    private String id;

    @Field(type = FieldType.Object, includeInParent = true)
    private JournalElastic journal;

    @Field(type = FieldType.Keyword)
    private String thumbnail;

    @Field(type = FieldType.Keyword)
    private Integer ojs_id;

    @Field(type = FieldType.Keyword)
    private String set_spec;

    @Field(type = FieldType.Nested, includeInParent = true)
    private Set<SubjectElastic> subjects;

    @Field(type = FieldType.Keyword)
    private String title;

    @Field(type = FieldType.Keyword)
    private String pages;

    @Field(type = FieldType.Keyword)
    private String publisher;

    @Field(type = FieldType.Keyword)
    private String publish_year;

    @Field(type = FieldType.Keyword)
    private String last_modifier;

    @Field(type = FieldType.Keyword)
    private String publish_date;

    @Field(type = FieldType.Keyword)
    private String issn;

    @Field(type = FieldType.Keyword)
    private String source_type;

    @Field(type = FieldType.Keyword)
    private String languange_publication = "en";

    @Field(type = FieldType.Keyword)
    private String doi;

    @Field(type = FieldType.Keyword)
    private String volume;

    @Field(type = FieldType.Keyword)
    private String issue;

    @Field(type = FieldType.Keyword)
    private String copyright;

    @MultiField(mainField = @Field(type = FieldType.Text, fielddata = true))
    private String abstract_text;

    @Field(type = FieldType.Keyword)
    private String article_pdf;

    @MultiField(mainField = @Field(type = FieldType.Text, fielddata = true))
    private String keyword;

    @Field(type = FieldType.Date)
    private Date updatedAt;

    @Field(type = FieldType.Date)
    private Date createdAt;

    @Field(type = FieldType.Nested, includeInParent = true)
    private Set<AuthorElastic> authors;

    @Field(type = FieldType.Object, includeInParent = true)
    private CitationScopusElastic citation_by_scopus;

    @Field(type = FieldType.Object, includeInParent = true)
    private CitationCrossRefElastic citation_by_cross_ref;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public JournalElastic getJournal() {
        return journal;
    }

    public void setJournal(JournalElastic journal) {
        this.journal = journal;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
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

    public Set<SubjectElastic> getSubjects() {
        return subjects;
    }

    public void setSubjects(Set<SubjectElastic> subjects) {
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

    public Set<AuthorElastic> getAuthors() {
        return authors;
    }

    public void setAuthors(Set<AuthorElastic> authors) {
        this.authors = authors;
    }

    public CitationScopusElastic getCitation_by_scopus() {
        return citation_by_scopus;
    }

    public void setCitation_by_scopus(CitationScopusElastic citation_by_scopus) {
        this.citation_by_scopus = citation_by_scopus;
    }

    public CitationCrossRefElastic getCitation_by_cross_ref() {
        return citation_by_cross_ref;
    }

    public void setCitation_by_cross_ref(CitationCrossRefElastic citation_by_cross_ref) {
        this.citation_by_cross_ref = citation_by_cross_ref;
    }

}
