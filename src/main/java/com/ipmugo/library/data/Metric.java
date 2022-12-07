package com.ipmugo.library.data;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "metric")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Metric {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private Double sjr;

    private Double snip;

    private Double citeScoreCurrent;

    private Double citeScoreTracker;

    private String currentYear;

    private String trackerYear;

    @OneToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "journal_id", nullable = false)
    private Journal journal;

    public Metric() {
    }

    public Metric(UUID id, Double sjr, Double snip, Double citeScoreCurrent, Double citeScoreTracker,
            String currentYear, String trackerYear, Journal journal) {
        this.id = id;
        this.sjr = sjr;
        this.snip = snip;
        this.citeScoreCurrent = citeScoreCurrent;
        this.citeScoreTracker = citeScoreTracker;
        this.currentYear = currentYear;
        this.trackerYear = trackerYear;
        this.journal = journal;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Double getSjr() {
        return sjr;
    }

    public void setSjr(Double sjr) {
        this.sjr = sjr;
    }

    public Double getSnip() {
        return snip;
    }

    public void setSnip(Double snip) {
        this.snip = snip;
    }

    public Double getCiteScoreCurrent() {
        return citeScoreCurrent;
    }

    public void setCiteScoreCurrent(Double citeScoreCurrent) {
        this.citeScoreCurrent = citeScoreCurrent;
    }

    public Double getCiteScoreTracker() {
        return citeScoreTracker;
    }

    public void setCiteScoreTracker(Double citeScoreTracker) {
        this.citeScoreTracker = citeScoreTracker;
    }

    public String getCurrentYear() {
        return currentYear;
    }

    public void setCurrentYear(String currentYear) {
        this.currentYear = currentYear;
    }

    public String getTrackerYear() {
        return trackerYear;
    }

    public void setTrackerYear(String trackerYear) {
        this.trackerYear = trackerYear;
    }

    public Journal getJournal() {
        return journal;
    }

    public void setJournal(Journal journal) {
        this.journal = journal;
    }

}
