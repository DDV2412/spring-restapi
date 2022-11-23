package com.ipmugo.library.data;

import java.util.UUID;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Figure {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String caption;

    private String base_url;

    public Figure() {
    }

    public Figure(UUID id, String caption, String base_url) {
        this.id = id;
        this.caption = caption;
        this.base_url = base_url;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public String getBase_url() {
        return base_url;
    }

    public void setBase_url(String base_url) {
        this.base_url = base_url;
    }

}
