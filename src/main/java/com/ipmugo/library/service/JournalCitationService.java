package com.ipmugo.library.service;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.ipmugo.library.data.Journal;
import com.ipmugo.library.data.JournalCitation;
import com.ipmugo.library.dto.CiteScoreYearInfoList;
import com.ipmugo.library.dto.Entry;
import com.ipmugo.library.dto.Example;
import com.ipmugo.library.dto.SJRList;
import com.ipmugo.library.dto.SNIPList;
import com.ipmugo.library.dto.SerialMetadataResponse;
import com.ipmugo.library.dto.Sjr;
import com.ipmugo.library.dto.Snip;
import com.ipmugo.library.repository.JournalCitationRepo;

import jakarta.transaction.TransactionScoped;

@Service
@TransactionScoped
public class JournalCitationService {

    @Autowired
    private JournalCitationRepo journalCitationRepo;

    @Autowired
    private JournalService journalService;

    private RestTemplate restTemplate = new RestTemplate();

    public JournalCitation save(JournalCitation journalCitation) {
        return journalCitationRepo.save(journalCitation);
    }

    public <T> JournalCitation citation(UUID id) {
        try {
            Journal journal = journalService.findOne(id);

            if (journal == null) {
                return null;
            }

            HttpHeaders headers = new HttpHeaders();
            headers.set("X-ELS-APIKey", "bb0f9584e36074a974a78c90396f08f5");

            HttpEntity<T> request = new HttpEntity<>(headers);

            ResponseEntity<Example> result = restTemplate.exchange(
                    "https://api.elsevier.com/content/serial/title?issn=" + journal.getIssn(),
                    HttpMethod.GET,
                    request,
                    new ParameterizedTypeReference<Example>() {
                    });

            if (result.getStatusCode().value() != 200) {
                return null;
            }

            Example data = result.getBody();

            if (data == null) {
                return null;
            }

            SerialMetadataResponse metadataResponse = data.getSerialMetadataResponse();

            List<Entry> entry = metadataResponse.getEntry();
            SJRList sjrList = entry.get(0).getSJRList();
            SNIPList snipList = entry.get(0).getSNIPList();
            CiteScoreYearInfoList citeScoreYearInfoList = entry.get(0).getCiteScoreYearInfoList();

            List<Sjr> sjr = sjrList.getSjr();
            List<Snip> snips = snipList.getSnip();

            Double sjrDouble = Double.parseDouble(sjr.get(0).get$());

            Double snipDouble = Double.parseDouble(snips.get(0).get$());

            Double citeScoreCurrent = Double.parseDouble(citeScoreYearInfoList.getCiteScoreCurrentMetric());

            Double citeScoreTrack = Double.parseDouble(citeScoreYearInfoList.getCiteScoreTracker());

            String currentYear = citeScoreYearInfoList.getCiteScoreCurrentMetricYear();

            String trackYear = citeScoreYearInfoList.getCiteScoreTrackerYear();

            JournalCitation journalCitation = new JournalCitation();
            journalCitation.setSjr(sjrDouble);
            journalCitation.setSnip(snipDouble);
            journalCitation.setCiteScoreCurrent(citeScoreCurrent);
            journalCitation.setCiteScoreTracker(citeScoreTrack);
            journalCitation.setCurrentYear(currentYear);
            journalCitation.setTrackerYear(trackYear);

            journal.setJournalCitation(journalCitation);
            journalService.save(journal);

            return save(journalCitation);
        } catch (Exception e) {
            return null;
        }
    }
}
