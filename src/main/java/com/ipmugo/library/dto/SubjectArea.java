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
        "@_fa",
        "@code",
        "@abbrev",
        "$"
})
@Generated("jsonschema2pojo")
public class SubjectArea {

    @JsonProperty("@_fa")
    private String fa;
    @JsonProperty("@code")
    private String code;
    @JsonProperty("@abbrev")
    private String abbrev;
    @JsonProperty("$")
    private String $;
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

    @JsonProperty("@code")
    public String getCode() {
        return code;
    }

    @JsonProperty("@code")
    public void setCode(String code) {
        this.code = code;
    }

    @JsonProperty("@abbrev")
    public String getAbbrev() {
        return abbrev;
    }

    @JsonProperty("@abbrev")
    public void setAbbrev(String abbrev) {
        this.abbrev = abbrev;
    }

    @JsonProperty("$")
    public String get$() {
        return $;
    }

    @JsonProperty("$")
    public void set$(String $) {
        this.$ = $;
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
