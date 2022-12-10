package com.ipmugo.library.data;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "citation_by_crossref")
public class CitationCrossRef {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private Integer references_count = 0;

    @OneToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "article_id", nullable = false)
    @JsonBackReference
    private Article article;

    public CitationCrossRef() {
    }

    public CitationCrossRef(UUID id, Integer references_count) {
        this.id = id;
        this.references_count = references_count;
    }

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

    public Article getArticle() {
        return article;
    }

    public void setArticle(Article article) {
        this.article = article;
    }

}