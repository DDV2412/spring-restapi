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
        "entry"
})
@Generated("jsonschema2pojo")
public class SerialMetadataResponse {

    @JsonProperty("entry")
    private List<EntryJournalCitation> entry = null;

    @JsonProperty("error")
    private String error;

    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("entry")
    public List<EntryJournalCitation> getEntry() {
        return entry;
    }

    @JsonProperty("entry")
    public void setEntry(List<EntryJournalCitation> entry) {
        this.entry = entry;
    }

    @JsonProperty("error")
    public String getError() {
        return error;
    }

    @JsonProperty("error")
    public void setError(String error) {
        this.error = error;
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