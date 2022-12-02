package com.ipmugo.library.dto;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.processing.Generated;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "subject-area",
        "SNIPList",
        "SJRList",
        "citeScoreYearInfoList",
})
@Generated("jsonschema2pojo")
public class EntryJournalCitation {

    @JsonProperty("subject-area")
    private List<SubjectArea> subjectArea = null;
    @JsonProperty("SNIPList")
    private SNIPList sNIPList;
    @JsonProperty("SJRList")
    private SJRList sJRList;
    @JsonProperty("citeScoreYearInfoList")
    private CiteScoreYearInfoList citeScoreYearInfoList;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

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

}