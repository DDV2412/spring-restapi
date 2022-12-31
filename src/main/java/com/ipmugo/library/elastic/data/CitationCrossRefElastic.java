package com.ipmugo.library.elastic.data;

import java.util.UUID;

import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import jakarta.persistence.Id;

@Document(indexName = "articles")
public class CitationCrossRefElastic {

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Integer getReferences_count() {
        return references_count;
    }

    public void setReferences_count(Integer references_count) {
        this.references_count = references_count;
    }

    @Id
    @Field(type = FieldType.Keyword)
    private UUID id;

    @Field(type = FieldType.Keyword)
    private Integer references_count = 0;

}