package com.ipmugo.library.dto;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.processing.Generated;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "@_fa",
        "dc:title",
        "dc:publisher",
        "coverageStartYear",
        "coverageEndYear",
        "prism:aggregationType",
        "source-id",
        "prism:issn",
        "prism:eIssn",
        "openaccess",
        "openaccessArticle",
        "openArchiveArticle",
        "openaccessType",
        "openaccessStartDate",
        "oaAllowsAuthorPaid",
        "subject-area",
        "SNIPList",
        "SJRList",
        "citeScoreYearInfoList",
        "link",
        "prism:url"
})
@Generated("jsonschema2pojo")
public class Entry {

    @JsonProperty("@_fa")
    private String fa;
    @JsonProperty("dc:title")
    private String dcTitle;
    @JsonProperty("dc:publisher")
    private String dcPublisher;
    @JsonProperty("coverageStartYear")
    private String coverageStartYear;
    @JsonProperty("coverageEndYear")
    private String coverageEndYear;
    @JsonProperty("prism:aggregationType")
    private String prismAggregationType;
    @JsonProperty("source-id")
    private String sourceId;
    @JsonProperty("prism:issn")
    private String prismIssn;
    @JsonProperty("prism:eIssn")
    private String prismEIssn;
    @JsonProperty("openaccess")
    private Object openaccess;
    @JsonProperty("openaccessArticle")
    private Object openaccessArticle;
    @JsonProperty("openArchiveArticle")
    private Object openArchiveArticle;
    @JsonProperty("openaccessType")
    private Object openaccessType;
    @JsonProperty("openaccessStartDate")
    private Object openaccessStartDate;
    @JsonProperty("oaAllowsAuthorPaid")
    private Object oaAllowsAuthorPaid;
    @JsonProperty("subject-area")
    private List<SubjectArea> subjectArea = null;
    @JsonProperty("SNIPList")
    private SNIPList sNIPList;
    @JsonProperty("SJRList")
    private SJRList sJRList;
    @JsonProperty("citeScoreYearInfoList")
    private CiteScoreYearInfoList citeScoreYearInfoList;
    @JsonProperty("link")
    private List<Link__1> link = null;
    @JsonProperty("prism:url")
    private String prismUrl;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("@_fa")
    public String getFa() {
        return fa;
    }

    @JsonProperty("@_fa")
    public void setFa(String fa) {
        this.fa = fa;
    }

    @JsonProperty("dc:title")
    public String getDcTitle() {
        return dcTitle;
    }

    @JsonProperty("dc:title")
    public void setDcTitle(String dcTitle) {
        this.dcTitle = dcTitle;
    }

    @JsonProperty("dc:publisher")
    public String getDcPublisher() {
        return dcPublisher;
    }

    @JsonProperty("dc:publisher")
    public void setDcPublisher(String dcPublisher) {
        this.dcPublisher = dcPublisher;
    }

    @JsonProperty("coverageStartYear")
    public String getCoverageStartYear() {
        return coverageStartYear;
    }

    @JsonProperty("coverageStartYear")
    public void setCoverageStartYear(String coverageStartYear) {
        this.coverageStartYear = coverageStartYear;
    }

    @JsonProperty("coverageEndYear")
    public String getCoverageEndYear() {
        return coverageEndYear;
    }

    @JsonProperty("coverageEndYear")
    public void setCoverageEndYear(String coverageEndYear) {
        this.coverageEndYear = coverageEndYear;
    }

    @JsonProperty("prism:aggregationType")
    public String getPrismAggregationType() {
        return prismAggregationType;
    }

    @JsonProperty("prism:aggregationType")
    public void setPrismAggregationType(String prismAggregationType) {
        this.prismAggregationType = prismAggregationType;
    }

    @JsonProperty("source-id")
    public String getSourceId() {
        return sourceId;
    }

    @JsonProperty("source-id")
    public void setSourceId(String sourceId) {
        this.sourceId = sourceId;
    }

    @JsonProperty("prism:issn")
    public String getPrismIssn() {
        return prismIssn;
    }

    @JsonProperty("prism:issn")
    public void setPrismIssn(String prismIssn) {
        this.prismIssn = prismIssn;
    }

    @JsonProperty("prism:eIssn")
    public String getPrismEIssn() {
        return prismEIssn;
    }

    @JsonProperty("prism:eIssn")
    public void setPrismEIssn(String prismEIssn) {
        this.prismEIssn = prismEIssn;
    }

    @JsonProperty("openaccess")
    public Object getOpenaccess() {
        return openaccess;
    }

    @JsonProperty("openaccess")
    public void setOpenaccess(Object openaccess) {
        this.openaccess = openaccess;
    }

    @JsonProperty("openaccessArticle")
    public Object getOpenaccessArticle() {
        return openaccessArticle;
    }

    @JsonProperty("openaccessArticle")
    public void setOpenaccessArticle(Object openaccessArticle) {
        this.openaccessArticle = openaccessArticle;
    }

    @JsonProperty("openArchiveArticle")
    public Object getOpenArchiveArticle() {
        return openArchiveArticle;
    }

    @JsonProperty("openArchiveArticle")
    public void setOpenArchiveArticle(Object openArchiveArticle) {
        this.openArchiveArticle = openArchiveArticle;
    }

    @JsonProperty("openaccessType")
    public Object getOpenaccessType() {
        return openaccessType;
    }

    @JsonProperty("openaccessType")
    public void setOpenaccessType(Object openaccessType) {
        this.openaccessType = openaccessType;
    }

    @JsonProperty("openaccessStartDate")
    public Object getOpenaccessStartDate() {
        return openaccessStartDate;
    }

    @JsonProperty("openaccessStartDate")
    public void setOpenaccessStartDate(Object openaccessStartDate) {
        this.openaccessStartDate = openaccessStartDate;
    }

    @JsonProperty("oaAllowsAuthorPaid")
    public Object getOaAllowsAuthorPaid() {
        return oaAllowsAuthorPaid;
    }

    @JsonProperty("oaAllowsAuthorPaid")
    public void setOaAllowsAuthorPaid(Object oaAllowsAuthorPaid) {
        this.oaAllowsAuthorPaid = oaAllowsAuthorPaid;
    }

    @JsonProperty("subject-area")
    public List<SubjectArea> getSubjectArea() {
        return subjectArea;
    }

    @JsonProperty("subject-area")
    public void setSubjectArea(List<SubjectArea> subjectArea) {
        this.subjectArea = subjectArea;
    }

    @JsonProperty("SNIPList")
    public SNIPList getSNIPList() {
        return sNIPList;
    }

    @JsonProperty("SNIPList")
    public void setSNIPList(SNIPList sNIPList) {
        this.sNIPList = sNIPList;
    }

    @JsonProperty("SJRList")
    public SJRList getSJRList() {
        return sJRList;
    }

    @JsonProperty("SJRList")
    public void setSJRList(SJRList sJRList) {
        this.sJRList = sJRList;
    }

    @JsonProperty("citeScoreYearInfoList")
    public CiteScoreYearInfoList getCiteScoreYearInfoList() {
        return citeScoreYearInfoList;
    }

    @JsonProperty("citeScoreYearInfoList")
    public void setCiteScoreYearInfoList(CiteScoreYearInfoList citeScoreYearInfoList) {
        this.citeScoreYearInfoList = citeScoreYearInfoList;
    }

    @JsonProperty("link")
    public List<Link__1> getLink() {
        return link;
    }

    @JsonProperty("link")
    public void setLink(List<Link__1> link) {
        this.link = link;
    }

    @JsonProperty("prism:url")
    public String getPrismUrl() {
        return prismUrl;
    }

    @JsonProperty("prism:url")
    public void setPrismUrl(String prismUrl) {
        this.prismUrl = prismUrl;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}