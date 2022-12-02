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
        "serial-metadata-response"
})
@Generated("jsonschema2pojo")
public class Example {

    @JsonProperty("serial-metadata-response")
    private SerialMetadataResponse serialMetadataResponse;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("serial-metadata-response")
    public SerialMetadataResponse getSerialMetadataResponse() {
        return serialMetadataResponse;
    }

    @JsonProperty("serial-metadata-response")
    public void setSerialMetadataResponse(SerialMetadataResponse serialMetadataResponse) {
        this.serialMetadataResponse = serialMetadataResponse;
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
