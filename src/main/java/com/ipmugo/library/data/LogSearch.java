package com.ipmugo.library.data;

import java.sql.Timestamp;
import java.util.Date;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "log_search")
public class LogSearch {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(length = 255, nullable = false)
    private String search;

    @CreationTimestamp
    private Timestamp updatedAt;

    @CreationTimestamp
    private Timestamp createdAt;

    public LogSearch() {
    }

    public LogSearch(UUID id, String search, Timestamp updatedAt, Timestamp createdAt) {
        this.id = id;
        this.search = search;
        this.updatedAt = updatedAt;
        this.createdAt = createdAt;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getSearch() {
        return search;
    }

    public void setSearch(String search) {
        this.search = search;
    }

    public Date getupdatedAt() {
        return updatedAt;
    }

    public void setupdatedAt(Timestamp updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Date getcreatedAt() {
        return createdAt;
    }

    public void setcreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

}
