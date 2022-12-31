package com.ipmugo.library.elastic.data;

import java.util.UUID;

import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import jakarta.persistence.Id;

@Document(indexName = "articles")
public class AuthorStatisticElastic {
    @Id
    @Field(type = FieldType.Keyword)
    private UUID id;

    @Field(type = FieldType.Keyword)
    private String citation;

    @Field(type = FieldType.Keyword)
    private String h_index;

    @Field(type = FieldType.Keyword)
    private String index_i10;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getCitation() {
        return citation;
    }

    public void setCitation(String citation) {
        this.citation = citation;
    }

    public String getH_index() {
        return h_index;
    }

    public void setH_index(String h_index) {
        this.h_index = h_index;
    }

    public String getIndex_i10() {
        return index_i10;
    }

    public void setIndex_i10(String index_i10) {
        this.index_i10 = index_i10;
    }

}
