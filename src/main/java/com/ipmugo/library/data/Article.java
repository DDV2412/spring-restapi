package com.ipmugo.library.data;

import java.time.Year;
import java.util.Date;
import java.util.UUID;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;

@Entity
public class Article {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    private Journal journal;

    @ManyToMany
    private Subject subject;

    private String title;

    private String pages;

    private String publisher;

    private Year publish_year;

    private Date publish_date;

    private String issn;

    private String source_type;

    private String languange_publication;

    private String doi;

    private Integer volume;

    private Integer issue;

    private String copyright;

    private String abstract_text;

    private String full_text;

    private Date updated_at;

    private Date created_at;

}
