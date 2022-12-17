package com.ipmugo.library.data;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

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
@Table(name = "author_static")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class AuthorStatistic {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @OneToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "author_id", nullable = false)
    @JsonBackReference
    private Author author;

    @Column(nullable = true, length = 255)
    private String citation;

    @Column(nullable = true, length = 255)
    private String h_index;

    @Column(nullable = true, length = 255)
    private String index_i10;

    public AuthorStatistic() {
    }

    public AuthorStatistic(UUID id, Author author, String citation, String h_index, String index_i10) {
        this.id = id;
        this.author = author;
        this.citation = citation;
        this.h_index = h_index;
        this.index_i10 = index_i10;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Author getAuthor() {
        return author;
    }

    public void setAuthor(Author author) {
        this.author = author;
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
