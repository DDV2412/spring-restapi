package com.ipmugo.library.data;

import java.util.Date;
import java.util.UUID;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;

@Entity
public class Journal {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String name;

    private String issn;

    private String e_issn;

    private String abbreviation;

    private String thumnail;

    private String description;

    private String journal_site;

    private String frequency;

    private String country;

    private String aim_scope_site;

    private String introduction_author_site;

    private String host_platform;

    private Integer issue_per_year;

    private String primary_languange;

    private String editor_site;

    private String full_text_format;

    private boolean article_doi;

    private String statement;

    private String license;

    private Double apc_fee;

    private String review_police;

    private Date updated_at;

    private Date created_at;

    @ManyToMany
    private Category category;
}
