package com.ipmugo.library.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.ipmugo.library.data.Article;
import com.ipmugo.library.data.Author;
import com.ipmugo.library.data.Category;
import com.ipmugo.library.data.Journal;
import com.ipmugo.library.data.Metric;
import com.ipmugo.library.data.Subject;
import com.ipmugo.library.dto.CiteScoreYearInfoList;
import com.ipmugo.library.dto.EntryJournalCitation;
import com.ipmugo.library.dto.ExampleJournalMetric;
import com.ipmugo.library.dto.SJRList;
import com.ipmugo.library.dto.SNIPList;
import com.ipmugo.library.dto.SerialMetadataResponse;
import com.ipmugo.library.dto.Sjr;
import com.ipmugo.library.dto.Snip;
import com.ipmugo.library.dto.SubjectArea;
import com.ipmugo.library.repository.ArticleRepo;
import com.ipmugo.library.repository.AuthorRepo;
import com.ipmugo.library.repository.CategoryRepo;
import com.ipmugo.library.repository.JournalRepo;
import com.ipmugo.library.repository.MetricRepo;
import com.ipmugo.library.repository.SubjectRepo;

@Component
@Async
public class ScheduledTask {

    @Autowired
    private JournalRepo journalRepo;

    @Autowired
    private MetricRepo metricRepo;

    @Autowired
    CategoryRepo categoryRepo;

    @Autowired
    private ArticleRepo articleRepo;

    @Autowired
    private AuthorRepo authorRepo;

    @Autowired
    private SubjectRepo subjectRepo;

    @Scheduled(cron = "0 0 0 10 * *", zone = "GMT+7")
    public <T> void journalMetric() {
        List<Journal> journals = journalRepo.findAll();

        RestTemplate restTemplate = new RestTemplate();

        if (journals.size() > 0) {
            for (int i = 0; i < journals.size(); i++) {
                HttpHeaders headers = new HttpHeaders();
                headers.set("X-ELS-APIKey", "bb0f9584e36074a974a78c90396f08f5");

                HttpEntity<T> request = new HttpEntity<>(headers);

                ResponseEntity<ExampleJournalMetric> result = restTemplate.exchange(
                        "https://api.elsevier.com/content/serial/title?issn=" + journals.get(i).getIssn(),
                        HttpMethod.GET,
                        request,
                        new ParameterizedTypeReference<ExampleJournalMetric>() {
                        });

                if (result.getStatusCode().value() == 200) {
                    ExampleJournalMetric data = result.getBody();

                    if (data != null) {
                        SerialMetadataResponse metadataResponse = data.getSerialMetadataResponse();

                        if (metadataResponse.getError() == null) {
                            List<EntryJournalCitation> entry = metadataResponse.getEntry();

                            if (entry.size() > 0) {
                                SJRList sjrList = entry.get(0).getSJRList();
                                SNIPList snipList = entry.get(0).getSNIPList();
                                CiteScoreYearInfoList citeScoreYearInfoList = entry.get(0).getCiteScoreYearInfoList();

                                if (sjrList != null && snipList != null && citeScoreYearInfoList != null) {
                                    List<Sjr> sjr = sjrList.getSjr();
                                    List<Snip> snips = snipList.getSnip();
                                    List<SubjectArea> subjectAreas = entry.get(0).getSubjectArea();

                                    Double sjrDouble = Double.parseDouble(sjr.get(0).get$());

                                    Double snipDouble = Double.parseDouble(snips.get(0).get$());

                                    Double citeScoreCurrent = Double
                                            .parseDouble(citeScoreYearInfoList.getCiteScoreCurrentMetric());

                                    Double citeScoreTrack = Double
                                            .parseDouble(citeScoreYearInfoList.getCiteScoreTracker());

                                    String currentYear = citeScoreYearInfoList.getCiteScoreCurrentMetricYear();

                                    String trackYear = citeScoreYearInfoList.getCiteScoreTrackerYear();

                                    List<Category> fixCategories = new ArrayList<>();

                                    if (subjectAreas.size() > 0) {
                                        for (int x = 0; x < subjectAreas.size(); x++) {
                                            Optional<Category> category = categoryRepo
                                                    .findByName(subjectAreas.get(x).get$());

                                            if (!category.isPresent()) {
                                                Category categoryValue = new Category();
                                                categoryValue.setName(subjectAreas.get(x).get$());
                                                fixCategories.add(x, categoryRepo.save(categoryValue));
                                            } else {
                                                fixCategories.add(x, category.get());
                                            }

                                        }
                                    }

                                    Optional<Metric> metric = metricRepo.findByJournalId(journals.get(i).getId());

                                    if (!metric.isPresent()) {
                                        Metric journalCitation = new Metric();
                                        journalCitation.setSjr(sjrDouble);
                                        journalCitation.setSnip(snipDouble);
                                        journalCitation.setCiteScoreCurrent(citeScoreCurrent);
                                        journalCitation.setCiteScoreTracker(citeScoreTrack);
                                        journalCitation.setCurrentYear(currentYear);
                                        journalCitation.setTrackerYear(trackYear);
                                        journalCitation.setJournal(journals.get(i));

                                        Metric metricResult = metricRepo.save(journalCitation);

                                        if (fixCategories.size() > 0) {
                                            journals.get(i).getCategories().addAll(fixCategories);
                                        }
                                        journals.get(i).setMetric(metricResult);
                                        journalRepo.save(journals.get(i));

                                        System.out.println(metricResult);
                                    }

                                    if (fixCategories.size() > 0) {
                                        journals.get(i).getCategories().addAll(fixCategories);
                                    }

                                    metric.get().setSjr(sjrDouble);
                                    metric.get().setSnip(snipDouble);
                                    metric.get().setCiteScoreCurrent(citeScoreCurrent);
                                    metric.get().setCiteScoreTracker(citeScoreTrack);
                                    metric.get().setCurrentYear(currentYear);
                                    metric.get().setTrackerYear(trackYear);
                                    metric.get().setJournal(journals.get(i));
                                    metricRepo.save(metric.get());

                                    journals.get(i).setMetric(metric.get());
                                    journalRepo.save(journals.get(i));

                                    System.out.println(metric.get());
                                }

                            }
                        }

                    }

                }
            }
        }
    }

    @Scheduled(cron = "0 0 0 20 * *", zone = "GMT+7")
    public void getHarvest() {
        try {
            List<Journal> journals = journalRepo.findAll();

            if (journals.size() > 0) {
                for (int i = 0; i < journals.size(); i++) {
                    Response response = Jsoup.connect(
                            journals.get(i).getJournal_site() +
                                    "/oai?verb=ListRecords&metadataPrefix=oai_dc&set="
                                    + journals
                                            .get(i)
                                            .getAbbreviation())
                            .timeout(0)
                            .execute();
                    Document document = Jsoup.connect(
                            journals.get(i).getJournal_site() +
                                    "/oai?verb=ListRecords&metadataPrefix=oai_dc&set="
                                    + journals
                                            .get(i)
                                            .getAbbreviation())
                            .timeout(0)
                            .get();

                    if (response.statusCode() == 200 && document.getElementsByTag("error").isEmpty()) {
                        parseData(journals.get(i), document);

                        if (!document.getElementsByTag("resumptionToken").isEmpty() && document
                                .getElementsByTag("resumptionToken").attr("expirationDate") == null) {
                            resumptionToken(journals.get(i),
                                    document.getElementsByTag("resumptionToken").first().text());
                        }
                    }

                }
            }

        } catch (Exception ex) {
            Logger.getLogger(ScheduledTask.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void resumptionToken(Journal journal, String resumptionToken) {
        try {

            Response response = Jsoup.connect(journal.getJournal_site() +
                    "/oai?verb=ListRecords&resumptionToken="
                    + resumptionToken)
                    .timeout(0)
                    .execute();
            Document document = Jsoup.connect(
                    journal.getJournal_site() +
                            "/oai?verb=ListRecords&resumptionToken="
                            + resumptionToken)
                    .timeout(0)
                    .get();

            if (response.statusCode() == 200 && document.getElementsByTag("error").isEmpty()) {
                parseData(journal, document);

                if (!document.getElementsByTag("resumptionToken").isEmpty() && document
                        .getElementsByTag("resumptionToken").attr("expirationDate") == null) {
                    resumptionToken(
                            journal,
                            document.getElementsByTag("resumptionToken").first().text());
                }
            }

        } catch (Exception ex) {
            Logger.getLogger(ScheduledTask.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void parseData(Journal journal, Document records) {

        Elements recordData = records.getElementsByTag("record");

        for (int i = 0; i < recordData.size(); i++) {
            if (recordData.get(i).getElementsByTag("header").attr("status") == "deleted") {
                continue;
            }

            if (recordData.get(i).getElementsByTag("metadata").isEmpty()) {
                continue;
            }

            Elements metadata = recordData.get(i).getElementsByTag("metadata");

            if (metadata.first().getElementsByTag("oai_dc:dc").isEmpty()) {
                continue;
            }

            Article articleData = new Article();

            Elements header = recordData.get(i).getElementsByTag("header");

            if (!header.first().getElementsByTag("identifier").isEmpty()
                    && header.first().getElementsByTag("identifier").first().text() != null) {
                articleData.setOjs_id(
                        Integer.parseInt(header.first().getElementsByTag("identifier").first().text().split("/")[1]));
            }

            if (!header.first().getElementsByTag("datestamp").isEmpty()
                    && header.first().getElementsByTag("datestamp").first().text() != null) {
                articleData.setLast_modifier(header.first().getElementsByTag("datestamp").first().text());
            }

            if (!header.first().getElementsByTag("setSpec").isEmpty()
                    && header.first().getElementsByTag("setSpec").first().text() != null) {
                articleData.setSet_spec(header.first().getElementsByTag("setSpec").first().text().split(":")[1]);
            }

            Elements dc = recordData.get(i).getElementsByTag("metadata").first().getElementsByTag("oai_dc:dc");

            if (!dc.first().getElementsByTag("dc:title").isEmpty()
                    && dc.first().getElementsByTag("dc:title").first().text() != null) {
                articleData.setTitle(dc.first().getElementsByTag("dc:title").first().text());
            }

            if (!dc.first().getElementsByTag("dc:publisher").isEmpty()
                    && dc.first().getElementsByTag("dc:publisher").first().text() != null) {
                articleData.setPublisher(dc.first().getElementsByTag("dc:publisher").first().text());
            }

            if (!dc.first().getElementsByTag("dc:date").isEmpty()
                    && dc.first().getElementsByTag("dc:date").first().text() != null) {
                articleData.setPublish_date(dc.first().getElementsByTag("dc:date").first().text());

            }

            if (articleData.getPublish_date() != null) {
                articleData.setPublish_year(articleData.getPublish_date().split("-")[0]);
            }

            if (!dc.first().getElementsByTag("dc:type").isEmpty()
                    && dc.first().getElementsByTag("dc:type").first().text() != null) {
                articleData.setSource_type(dc.first().getElementsByTag("dc:type").first().text());

            }

            if (!dc.first().getElementsByTag("dc:language").isEmpty()
                    && dc.first().getElementsByTag("dc:language").first().text() != null) {
                articleData.setLanguange_publication(dc.first().getElementsByTag("dc:language").first().text());

            }

            Elements sources = dc.first().getElementsByTag("dc:source");

            if (!sources.isEmpty()) {
                articleData.setIssn(sources.get(2).text());

                if (sources.first().text().split(";").length == 3) {
                    articleData.setVolume(sources.first().text().split(";")[1].split(",")[0].split("Vol ")[1]);
                    articleData
                            .setIssue(sources.first().text().split(";")[1].split(",")[1].split(":")[0].split("No ")[1]);
                    articleData.setPages(sources.first().text().split(";")[2]);
                }
            }

            if (!dc.first().getElementsByTag("dc:description").isEmpty()
                    && dc.first().getElementsByTag("dc:description").first().text() != null) {
                articleData.setAbstract_text(dc.first().getElementsByTag("dc:description").first().text());
            }

            if (!dc.first().getElementsByTag("dc:identifier").isEmpty()
                    && dc.first().getElementsByTag("dc:identifier").last().text() != null) {
                articleData.setDoi(dc.first().getElementsByTag("dc:identifier").last().text());
            }

            if (articleData.getAbstract_text() != null && articleData.getAbstract_text().split("DOI:").length > 2) {
                articleData.setAbstract_text(articleData.getAbstract_text().split("DOI:")[0]);

                if (!articleData.getAbstract_text().split("DOI:")[1].isEmpty()
                        && articleData.getAbstract_text().split("DOI:")[1] != null) {
                    if (articleData.getAbstract_text().split("DOI:")[1].split("doi.org/").length > 2) {
                        articleData.setDoi(articleData.getAbstract_text().split("DOI:")[1].split("doi.org/")[1]);
                    } else {
                        articleData.setDoi(articleData.getAbstract_text().split("DOI:")[1].split("doi.org/")[0]);
                    }
                }
            }

            String keyword = null;

            String subjectsData = null;

            Elements subject = dc.first().getElementsByTag("dc:subject");

            if (!subject.isEmpty() && subject.size() > 1) {
                if (!subject.isEmpty()) {
                    subjectsData = subject.get(0).text();

                    keyword = subject.get(1).text();
                }
            } else {
                if (!subject.isEmpty()) {
                    keyword = subject.first().text();
                }
            }

            if (keyword != null) {
                articleData.setKeyword(keyword);
            }

            if (!dc.first()
                    .getElementsByTag("dc:relation").isEmpty()
                    && dc.first()
                            .getElementsByTag("dc:relation").size() > 0) {
                articleData.setArticle_pdf(dc.first()
                        .getElementsByTag("dc:relation").first().text().replaceAll("/view/",
                                "/download/"));
            }

            if (!dc.first()
                    .getElementsByTag("dc:rights").isEmpty()
                    && dc.first()
                            .getElementsByTag("dc:rights").size() > 0) {
                articleData.setCopyright(dc.first()
                        .getElementsByTag("dc:rights").last().text());
            }

            Elements creator = dc.first().getElementsByTag("dc:creator");

            if (subjectsData != null) {
                subjectsData = subjectsData.replaceAll("; ", ";");
                subjectsData = subjectsData.replaceAll(", ", ";");
                subjectsData = subjectsData.replaceAll(",", ";");

                for (int t = 0; t < subjectsData.split(";").length; t++) {
                    if (subjectsData.split(";")[t].trim() != null && !subjectsData.split(";")[t].trim().isEmpty()) {
                        Subject subjectRes = new Subject();
                        subjectRes.setName(subjectsData.split(";")[t].trim());
                        Optional<Subject> subjectOptional = subjectRepo.findByName(subjectRes.getName());

                        if (!subjectOptional.isPresent()) {
                            subjectRepo.save(subjectRes);
                        }
                    }
                }
            }

            if (articleData.getDoi() != null) {
                Optional<Article> article = articleRepo.findByDoi(articleData.getDoi());

                if (!article.isPresent()) {
                    articleData.setJournal(journal);

                    Article article2 = articleRepo.save(articleData);

                    for (int x = 0; x < creator.size(); x++) {
                        Author author = new Author();
                        author.setFirst_name(creator.get(x).text().split(", ")[1]);
                        author.setLast_name(creator.get(x).text().split(", ")[0]);
                        author.setArticle(article2);
                        authorRepo.save(author);
                    }

                    if (subjectsData != null) {
                        subjectsData = subjectsData.replaceAll("; ", ";");
                        subjectsData = subjectsData.replaceAll(", ", ";");
                        subjectsData = subjectsData.replaceAll(",", ";");

                        for (int t = 0; t < subjectsData.split(";").length; t++) {
                            if (subjectsData.split(";")[t].trim() != null
                                    && !subjectsData.split(";")[t].trim().isEmpty()) {
                                Subject subjectRes = new Subject();
                                subjectRes.setName(subjectsData.split(";")[t].trim());
                                Optional<Subject> subjectOptional = subjectRepo.findByName(subjectRes.getName());

                                if (subjectOptional.isPresent()) {
                                    subjectOptional.get().getArticles().add(article2);

                                    subjectRepo.save(subjectOptional.get());
                                }
                            }
                        }
                    }
                } else {
                    article.get().setOjs_id(articleData.getOjs_id());
                    article.get().setLast_modifier(articleData.getLast_modifier());
                    article.get().setSet_spec(articleData.getSet_spec());
                    article.get().setTitle(articleData.getTitle());
                    article.get().setAbstract_text(articleData.getAbstract_text());
                    article.get().setPublisher(articleData.getPublisher());
                    article.get().setPublish_date(articleData.getPublish_date());
                    article.get().setPublish_year(articleData.getPublish_year());
                    article.get().setSource_type(articleData.getSource_type());
                    article.get().setDoi(articleData.getDoi());
                    article.get().setLanguange_publication(articleData.getLanguange_publication());
                    article.get().setArticle_pdf(articleData.getArticle_pdf());
                    article.get().setCopyright(articleData.getCopyright());
                    article.get().setIssn(articleData.getIssn());
                    article.get().setIssue(articleData.getIssue());
                    article.get().setVolume(articleData.getVolume());
                    article.get().setPages(articleData.getPages());
                    article.get().setKeyword(articleData.getKeyword());
                    article.get().setJournal(journal);
                    articleRepo.save(article.get());

                    for (int x = 0; x < creator.size(); x++) {
                        Author author = new Author();
                        author.setFirst_name(creator.get(x).text().split(", ")[1]);
                        author.setLast_name(creator.get(x).text().split(", ")[0]);
                        author.setArticle(article.get());
                        authorRepo.save(author);
                    }

                    if (subjectsData != null) {
                        subjectsData = subjectsData.replaceAll("; ", ";");
                        subjectsData = subjectsData.replaceAll(", ", ";");
                        subjectsData = subjectsData.replaceAll(",", ";");

                        for (int t = 0; t < subjectsData.split(";").length; t++) {
                            if (subjectsData.split(";")[t].trim() != null
                                    && !subjectsData.split(";")[t].trim().isEmpty()) {
                                Subject subjectRes = new Subject();
                                subjectRes.setName(subjectsData.split(";")[t].trim());
                                Optional<Subject> subjectOptional = subjectRepo.findByName(subjectRes.getName());

                                if (subjectOptional.isPresent()) {
                                    subjectOptional.get().getArticles().add(article.get());

                                    subjectRepo.save(subjectOptional.get());
                                }
                            }
                        }
                    }

                    articleRepo.save(article.get());
                }
            }

        }

    }
}
