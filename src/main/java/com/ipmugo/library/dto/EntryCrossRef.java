package com.ipmugo.library.dto;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.processing.Generated;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({

        "references-count",

})
@Generated("jsonschema2pojo")
public class EntryCrossRef {

    @JsonProperty("references-count")
    private Integer referencesCount;

    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("references-count")
    public Integer getReferencesCount() {
        return referencesCount;
    }

    @JsonProperty("references-count")
    public void setReferencesCount(Integer referencesCount) {
        this.referencesCount = referencesCount;
    }

}