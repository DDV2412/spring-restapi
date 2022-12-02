package com.ipmugo.library.dto;

import java.time.Year;
import java.util.Date;

public class ArticleData {

    private Integer ojs_id;

    private String set_spec;

    private String figure;

    private String title;

    private String pages;

    private String publisher;

    private Year publish_year;

    private Date publish_date;

    private String issn;

    private String source_type;

    private String languange_publication = "en";

    private String doi;

    private Integer volume;

    private Integer issue;

    private String copyright;

    private String abstract_text;

    private String full_text;

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

}
