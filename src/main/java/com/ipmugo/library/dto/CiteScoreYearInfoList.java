package com.ipmugo.library.dto;

import java.util.HashMap;
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
        "citeScoreCurrentMetric",
        "citeScoreCurrentMetricYear",
        "citeScoreTracker",
        "citeScoreTrackerYear"
})
@Generated("jsonschema2pojo")
public class CiteScoreYearInfoList {

    @JsonProperty("citeScoreCurrentMetric")
    private String citeScoreCurrentMetric;
    @JsonProperty("citeScoreCurrentMetricYear")
    private String citeScoreCurrentMetricYear;
    @JsonProperty("citeScoreTracker")
    private String citeScoreTracker;
    @JsonProperty("citeScoreTrackerYear")
    private String citeScoreTrackerYear;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("citeScoreCurrentMetric")
    public String getCiteScoreCurrentMetric() {
        return citeScoreCurrentMetric;
    }

    @JsonProperty("citeScoreCurrentMetric")
    public void setCiteScoreCurrentMetric(String citeScoreCurrentMetric) {
        this.citeScoreCurrentMetric = citeScoreCurrentMetric;
    }

    @JsonProperty("citeScoreCurrentMetricYear")
    public String getCiteScoreCurrentMetricYear() {
        return citeScoreCurrentMetricYear;
    }

    @JsonProperty("citeScoreCurrentMetricYear")
    public void setCiteScoreCurrentMetricYear(String citeScoreCurrentMetricYear) {
        this.citeScoreCurrentMetricYear = citeScoreCurrentMetricYear;
    }

    @JsonProperty("citeScoreTracker")
    public String getCiteScoreTracker() {
        return citeScoreTracker;
    }

    @JsonProperty("citeScoreTracker")
    public void setCiteScoreTracker(String citeScoreTracker) {
        this.citeScoreTracker = citeScoreTracker;
    }

    @JsonProperty("citeScoreTrackerYear")
    public String getCiteScoreTrackerYear() {
        return citeScoreTrackerYear;
    }

    @JsonProperty("citeScoreTrackerYear")
    public void setCiteScoreTrackerYear(String citeScoreTrackerYear) {
        this.citeScoreTrackerYear = citeScoreTrackerYear;
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
