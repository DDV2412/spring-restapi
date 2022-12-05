package com.ipmugo.library.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.ipmugo.library.data.Category;
import com.ipmugo.library.data.Journal;
import com.ipmugo.library.data.Metric;
import com.ipmugo.library.dto.CiteScoreYearInfoList;
import com.ipmugo.library.dto.EntryJournalCitation;
import com.ipmugo.library.dto.ExampleJournalMetric;
import com.ipmugo.library.dto.SJRList;
import com.ipmugo.library.dto.SNIPList;
import com.ipmugo.library.dto.SerialMetadataResponse;
import com.ipmugo.library.dto.Sjr;
import com.ipmugo.library.dto.Snip;
import com.ipmugo.library.dto.SubjectArea;
import com.ipmugo.library.repository.CategoryRepo;
import com.ipmugo.library.repository.JournalRepo;

import jakarta.transaction.TransactionScoped;

@Service
@TransactionScoped
public class JournalService {

    @Autowired
    private JournalRepo journalRepo;

    @Autowired
    private CategoryRepo categoryRepo;

    private RestTemplate restTemplate = new RestTemplate();

    public List<Journal> findAll() {
        return journalRepo.findAll();
    }

    public Journal findOne(UUID id) {
        Optional<Journal> journal = journalRepo.findById(id);

        if (!journal.isPresent()) {
            return null;
        }

        return journal.get();
    }

    public Journal findByIssn(String issn) {
        Optional<Journal> journal = journalRepo.findByIssn(issn);

        if (!journal.isPresent()) {
            return null;
        }

        return journal.get();
    }

    public Journal findByName(String name) {
        Optional<Journal> journal = journalRepo.findByName(name);

        if (!journal.isPresent()) {
            return null;
        }

        return journal.get();
    }

    public Journal save(Journal journal) {
        return journalRepo.save(journal);
    }

    public void deleteById(UUID id) {
        journalRepo.deleteById(id);
    }

    public Journal setCategory(UUID id, Category category) {
        Journal journal = findOne(id);

        journal.getCategories().add(category);
        save(journal);

        return journal;
    }

    public Journal setCategories(UUID id, Collection<Category> categories) {
        Journal journal = findOne(id);

        journal.getCategories().addAll(categories);
        save(journal);

        return journal;
    }

    public <T> Metric citation(Journal journal) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set("X-ELS-APIKey", "bb0f9584e36074a974a78c90396f08f5");

            HttpEntity<T> request = new HttpEntity<>(headers);

            ResponseEntity<ExampleJournalMetric> result = restTemplate.exchange(
                    "https://api.elsevier.com/content/serial/title?issn=" + journal.getIssn(),
                    HttpMethod.GET,
                    request,
                    new ParameterizedTypeReference<ExampleJournalMetric>() {
                    });

            if (result.getStatusCode().value() != 200) {
                return null;
            }

            ExampleJournalMetric data = result.getBody();

            if (data == null) {
                return null;
            }

            SerialMetadataResponse metadataResponse = data.getSerialMetadataResponse();

            if (metadataResponse.getError() != null) {
                return null;
            }

            List<EntryJournalCitation> entry = metadataResponse.getEntry();
            SJRList sjrList = entry.get(0).getSJRList();
            SNIPList snipList = entry.get(0).getSNIPList();
            CiteScoreYearInfoList citeScoreYearInfoList = entry.get(0).getCiteScoreYearInfoList();

            List<Sjr> sjr = sjrList.getSjr();
            List<Snip> snips = snipList.getSnip();
            List<SubjectArea> subjectAreas = entry.get(0).getSubjectArea();

            Double sjrDouble = Double.parseDouble(sjr.get(0).get$());

            Double snipDouble = Double.parseDouble(snips.get(0).get$());

            Double citeScoreCurrent = Double.parseDouble(citeScoreYearInfoList.getCiteScoreCurrentMetric());

            Double citeScoreTrack = Double.parseDouble(citeScoreYearInfoList.getCiteScoreTracker());

            String currentYear = citeScoreYearInfoList.getCiteScoreCurrentMetricYear();

            String trackYear = citeScoreYearInfoList.getCiteScoreTrackerYear();

            List<Category> fixCategories = new ArrayList<>();

            if (subjectAreas.size() > 0) {
                for (int i = 0; i < subjectAreas.size(); i++) {
                    Optional<Category> category = categoryRepo.findByName(subjectAreas.get(i).get$());

                    if (!category.isPresent()) {
                        Category categoryValue = new Category();
                        categoryValue.setName(subjectAreas.get(i).get$());
                        fixCategories.add(i, categoryRepo.save(categoryValue));
                    } else {
                        fixCategories.add(i, category.get());
                    }

                }
            }

            Metric journalCitation = new Metric();
            journalCitation.setSjr(sjrDouble);
            journalCitation.setSnip(snipDouble);
            journalCitation.setCiteScoreCurrent(citeScoreCurrent);
            journalCitation.setCiteScoreTracker(citeScoreTrack);
            journalCitation.setCurrentYear(currentYear);
            journalCitation.setTrackerYear(trackYear);
            journalCitation.setJournal(journal);

            if (fixCategories.size() > 0) {
                journal.getCategories().addAll(fixCategories);
            }

            journal.setMetric(journalCitation);
            save(journal);

            return journalCitation;
        } catch (Exception e) {
            return null;
        }
    }

}
