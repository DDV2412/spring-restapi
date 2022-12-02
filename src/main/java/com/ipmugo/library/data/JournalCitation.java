package com.ipmugo.library.data;

import java.util.UUID;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class JournalCitation {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private Double sjr;

    private Double snip;

    private Double citeScoreCurrent;

    private Double citeScoreTracker;

    private String currentYear;

    private String trackerYear;

    public JournalCitation() {
    }

    public JournalCitation(UUID id, Double sjr, Double snip, Double citeScoreCurrent, Double citeScoreTracker,
            String currentYear, String trackerYear) {
        this.id = id;
        this.sjr = sjr;
        this.snip = snip;
        this.citeScoreCurrent = citeScoreCurrent;
        this.citeScoreTracker = citeScoreTracker;
        this.currentYear = currentYear;
        this.trackerYear = trackerYear;
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

}
