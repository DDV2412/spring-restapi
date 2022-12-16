package com.ipmugo.library.utils;

import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.ImageIO;

import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDResources;
import org.apache.pdfbox.pdmodel.graphics.PDXObject;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.apache.pdfbox.text.PDFTextStripper;
import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import com.ipmugo.library.data.Article;
import com.ipmugo.library.data.Author;
import com.ipmugo.library.data.Category;
import com.ipmugo.library.data.CitationCrossRef;
import com.ipmugo.library.data.CitationScopus;
import com.ipmugo.library.data.Journal;
import com.ipmugo.library.data.Metric;
import com.ipmugo.library.data.Subject;
import com.ipmugo.library.dto.CiteScoreYearInfoList;
import com.ipmugo.library.dto.EntryCrossRef;
import com.ipmugo.library.dto.EntryJournalCitation;
import com.ipmugo.library.dto.EntryScopus;
import com.ipmugo.library.dto.ExampleCitationCrossRef;
import com.ipmugo.library.dto.ExampleCitationScopus;
import com.ipmugo.library.dto.ExampleJournalMetric;
import com.ipmugo.library.dto.SJRList;
import com.ipmugo.library.dto.SNIPList;
import com.ipmugo.library.dto.SearchResults;
import com.ipmugo.library.dto.SerialMetadataResponse;
import com.ipmugo.library.dto.Sjr;
import com.ipmugo.library.dto.Snip;
import com.ipmugo.library.dto.SubjectArea;
import com.ipmugo.library.elastic.data.ArticleElastic;
import com.ipmugo.library.elastic.data.JournalElastic;
import com.ipmugo.library.elastic.repository.ArticleElasticRepo;
import com.ipmugo.library.repository.ArticleRepo;
import com.ipmugo.library.repository.AuthorRepo;
import com.ipmugo.library.repository.CategoryRepo;
import com.ipmugo.library.repository.CitationCrossRefRepo;
import com.ipmugo.library.repository.CitationScopusRepo;
import com.ipmugo.library.repository.JournalRepo;
import com.ipmugo.library.repository.MetricRepo;
import com.ipmugo.library.repository.SubjectRepo;

import co.elastic.clients.elasticsearch.core.IndexResponse;

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
    private CitationScopusRepo citationScopusRepo;

    @Autowired
    private CitationCrossRefRepo citationCrossRefRepo;

    @Autowired
    private ArticleElasticRepo elasticRepo;

    @Autowired
    private SubjectRepo subjectRepo;

    @Autowired
    private AuthorRepo authorRepo;

    @Scheduled(cron = "0 0 0 10 * *", zone = "GMT+7")
    public <T> void journalMetric() {
        Pageable pageable = PageRequest.of(0, 15);

        Page<Journal> journals = journalRepo.findAll(pageable);

        RestTemplate restTemplate = new RestTemplate();

        for (int i = 0; i < journals.getTotalPages(); i++) {
            if (journals.getContent().size() > 0) {
                for (Journal journal : journals.getContent()) {
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

                        if (result.getStatusCode().value() == 200) {
                            ExampleJournalMetric data = result.getBody();

                            if (data != null) {
                                SerialMetadataResponse metadataResponse = data.getSerialMetadataResponse();

                                if (metadataResponse.getError() == null) {
                                    List<EntryJournalCitation> entry = metadataResponse.getEntry();

                                    if (entry.size() > 0) {
                                        SJRList sjrList = entry.get(0).getSJRList();
                                        SNIPList snipList = entry.get(0).getSNIPList();
                                        CiteScoreYearInfoList citeScoreYearInfoList = entry.get(0)
                                                .getCiteScoreYearInfoList();

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

                                            if (subjectAreas.size() > 0) {
                                                for (int x = 0; x < subjectAreas.size(); x++) {
                                                    Optional<Category> category = categoryRepo
                                                            .findByName(subjectAreas.get(x).get$());

                                                    if (!category.isPresent()) {
                                                        Category categoryValue = new Category();
                                                        categoryValue.setName(subjectAreas.get(x).get$());
                                                        categoryRepo.save(categoryValue);
                                                    }

                                                }
                                            }

                                            Optional<Metric> metric = metricRepo.findByJournalId(journal.getId());

                                            if (!metric.isPresent()) {
                                                Metric journalCitation = new Metric();
                                                journalCitation.setSjr(sjrDouble);
                                                journalCitation.setSnip(snipDouble);
                                                journalCitation.setCiteScoreCurrent(citeScoreCurrent);
                                                journalCitation.setCiteScoreTracker(citeScoreTrack);
                                                journalCitation.setCurrentYear(currentYear);
                                                journalCitation.setTrackerYear(trackYear);
                                                journalCitation.setJournal(journal);

                                                Metric metricResult = metricRepo.save(journalCitation);

                                                if (subjectAreas.size() > 0) {
                                                    for (int x = 0; x < subjectAreas.size(); x++) {
                                                        Optional<Category> category = categoryRepo
                                                                .findByName(subjectAreas.get(x).get$());

                                                        if (category.isPresent()) {
                                                            category.get().getJournals().add(journal);
                                                            categoryRepo.save(category.get());
                                                        }

                                                    }
                                                }
                                                System.out.println(metricResult);
                                            } else {
                                                metric.get().setSjr(sjrDouble);
                                                metric.get().setSnip(snipDouble);
                                                metric.get().setCiteScoreCurrent(citeScoreCurrent);
                                                metric.get().setCiteScoreTracker(citeScoreTrack);
                                                metric.get().setCurrentYear(currentYear);
                                                metric.get().setTrackerYear(trackYear);
                                                metric.get().setJournal(journal);
                                                metricRepo.save(metric.get());

                                                if (subjectAreas.size() > 0) {
                                                    for (int x = 0; x < subjectAreas.size(); x++) {
                                                        Optional<Category> category = categoryRepo
                                                                .findByName(subjectAreas.get(x).get$());

                                                        if (category.isPresent()) {
                                                            category.get().getJournals().add(journal);
                                                            categoryRepo.save(category.get());
                                                        }

                                                    }
                                                }

                                                System.out.println(metric.get());
                                            }

                                        }

                                    }
                                }

                            }

                        }
                    } catch (Exception e) {
                        continue;
                    }
                }
            }
        }
    }

    @Scheduled(cron = "0 0 0 15 * *", zone = "GMT+7")
    public void getHarvest() {
        try {
            Pageable pageable = PageRequest.of(0, 15);

            Page<Journal> journals = journalRepo.findAll(pageable);

            for (int i = 0; i < journals.getTotalPages(); i++) {
                if (journals.getContent().size() > 0) {
                    for (Journal journal : journals.getContent()) {
                        Response response = Jsoup.connect(
                                journal.getJournal_site() +
                                        "/oai?verb=ListRecords&metadataPrefix=oai_dc&set="
                                        + journal.getAbbreviation())
                                .timeout(0)
                                .execute();
                        Document document = Jsoup.connect(
                                journal.getJournal_site() +
                                        "/oai?verb=ListRecords&metadataPrefix=oai_dc&set="
                                        + journal
                                                .getAbbreviation())
                                .timeout(0)
                                .get();

                        if (response.statusCode() == 200 && document.getElementsByTag("error").isEmpty()) {
                            parseData(journal, document);

                            if (!document.getElementsByTag("resumptionToken").isEmpty() && document
                                    .getElementsByTag("resumptionToken").attr("expirationDate") != null) {
                                resumptionToken(
                                        journal,
                                        document.getElementsByTag("resumptionToken").get(0).text());
                            }
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
                        .getElementsByTag("resumptionToken").attr("expirationDate") != null) {
                    resumptionToken(
                            journal,
                            document.getElementsByTag("resumptionToken").get(0).text());
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

            if (metadata.get(0).getElementsByTag("oai_dc:dc").isEmpty()) {
                continue;
            }

            Article articleData = new Article();

            Elements dc = metadata.get(0).getElementsByTag("oai_dc:dc");

            if (!recordData.get(i).getElementsByTag("header").get(0).getElementsByTag("identifier").isEmpty()
                    && recordData.get(i).getElementsByTag("header").get(0).getElementsByTag("identifier")
                            .text() != null) {
                articleData.setOjs_id(
                        Integer.parseInt(recordData.get(i).getElementsByTag("header").get(0)
                                .getElementsByTag("identifier").get(0).text().split("/")[1]));
            }

            if (!recordData.get(i).getElementsByTag("header").get(0).getElementsByTag("datestamp").isEmpty()
                    && recordData.get(i).getElementsByTag("header").get(0).getElementsByTag("datestamp").get(0)
                            .text() != null) {
                articleData.setLast_modifier(recordData.get(i).getElementsByTag("header").get(0)
                        .getElementsByTag("datestamp").get(0).text());
            }

            if (!recordData.get(i).getElementsByTag("header").get(0).getElementsByTag("setSpec").isEmpty()
                    && recordData.get(i).getElementsByTag("header").get(0).getElementsByTag("setSpec").get(0)
                            .text() != null) {
                articleData.setSet_spec(recordData.get(i).getElementsByTag("header").get(0).getElementsByTag("setSpec")
                        .get(0).text().split(":")[1]);
            }

            if (!dc.get(0).getElementsByTag("dc:title").isEmpty()
                    && dc.get(0).getElementsByTag("dc:title").get(0).text() != null) {
                articleData.setTitle(dc.get(0).getElementsByTag("dc:title").get(0).text());
            }

            if (!dc.get(0).getElementsByTag("dc:publisher").isEmpty()
                    && dc.get(0).getElementsByTag("dc:publisher").get(0).text() != null) {
                articleData.setPublisher(dc.get(0).getElementsByTag("dc:publisher").get(0).text());
            }

            if (!dc.get(0).getElementsByTag("dc:date").isEmpty()
                    && dc.get(0).getElementsByTag("dc:date").get(0).text() != null) {
                articleData.setPublish_date(dc.get(0).getElementsByTag("dc:date").get(0).text());

            }

            if (articleData.getPublish_date() != null) {
                articleData.setPublish_year(articleData.getPublish_date().split("-")[0]);
            }

            if (!dc.get(0).getElementsByTag("dc:type").isEmpty()
                    && dc.get(0).getElementsByTag("dc:type").get(0).text() != null) {
                articleData.setSource_type(dc.get(0).getElementsByTag("dc:type").get(0).text());

            }

            if (!dc.get(0).getElementsByTag("dc:language").isEmpty()
                    && dc.get(0).getElementsByTag("dc:language").get(0).text() != null) {
                articleData.setLanguange_publication(dc.get(0).getElementsByTag("dc:language").get(0).text());

            }

            Elements sources = dc.get(0).getElementsByTag("dc:source");

            if (!sources.isEmpty()) {
                articleData.setIssn(sources.get(2).text());

                if (sources.get(0).text().split(";").length == 3) {
                    articleData.setVolume(sources.get(0).text().split(";")[1].split(",")[0].split("Vol ")[1]);
                    articleData
                            .setIssue(sources.get(0).text().split(";")[1].split(",")[1].split(":")[0]
                                    .split("No ")[1]);
                    articleData.setPages(sources.get(0).text().split(";")[2]);
                }
            }

            if (!dc.get(0).getElementsByTag("dc:description").isEmpty()
                    && dc.get(0).getElementsByTag("dc:description").get(0).text() != null) {
                articleData.setAbstract_text(dc.get(0).getElementsByTag("dc:description").get(0).text());
            }

            if (!dc.get(0).getElementsByTag("dc:identifier").isEmpty()
                    && dc.get(0).getElementsByTag("dc:identifier").size() == 2 && dc.get(0)
                            .getElementsByTag("dc:identifier").get(1).text() != null) {
                articleData.setDoi(dc.get(0).getElementsByTag("dc:identifier").get(1).text());
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

            Elements subject = dc.get(0).getElementsByTag("dc:subject");

            if (!subject.isEmpty() && subject.size() == 2) {
                if (!subject.isEmpty()) {

                    subjectsData = subject.get(0).text();

                    subjectsData = subjectsData.replaceAll("; ", ";");
                    subjectsData = subjectsData.replaceAll(", ", ";");
                    subjectsData = subjectsData.replaceAll(",", ";");

                    keyword = subject.get(1).text();

                    keyword = keyword.replaceAll("; ", ";");
                    keyword = keyword.replaceAll(", ", ";");
                    keyword = keyword.replaceAll(",", ";");
                }
            } else {
                if (!subject.isEmpty()) {
                    keyword = subject.get(0).text();

                    keyword = keyword.replaceAll("; ", ";");
                    keyword = keyword.replaceAll(", ", ";");
                    keyword = keyword.replaceAll(",", ";");
                }
            }

            if (keyword != null) {
                articleData.setKeyword(keyword);
            }

            if (!dc.get(0)
                    .getElementsByTag("dc:relation").isEmpty()
                    && dc.get(0)
                            .getElementsByTag("dc:relation").size() > 0) {
                articleData.setArticle_pdf(dc.get(0)
                        .getElementsByTag("dc:relation").get(0).text().replaceAll("/view/",
                                "/download/"));
            }

            if (!dc.get(0)
                    .getElementsByTag("dc:rights").isEmpty()
                    && dc.get(0)
                            .getElementsByTag("dc:rights").size() == 2) {
                articleData.setCopyright(dc.get(0)
                        .getElementsByTag("dc:rights").get(1).text());
            }

            if (articleData.getArticle_pdf() != null && articleData.getDoi() != null) {
                Optional<Article> article = articleRepo.findByDoi(articleData.getDoi());

                if (!article.isPresent()) {

                    if (articleData.getArticle_pdf() != null
                            && !articleData.getArticle_pdf().contains("downloadSuppFile")
                            && !articleData.getArticle_pdf()
                                    .contains("info")) {
                        try {
                            URL url = new URL(articleData.getArticle_pdf());
                            InputStream inputStream = url.openStream();
                            PDDocument document = PDDocument.load(inputStream);
                            if (document.isEncrypted()) {
                                document.setAllSecurityToBeRemoved(false);
                            }
                            PDFTextStripper stripper = new PDFTextStripper();
                            String text = stripper.getText(document);
                            document.close();

                            String[] lines = text.split("\n");
                            String paragraph = "";
                            String startElement = "1. INTRODUCTION";
                            String endElement = "BIOGRAPHIES OF AUTHORS";
                            boolean foundStart = false;
                            boolean foundEnd = false;
                            for (String line : lines) {
                                if (line.contains(startElement)) {
                                    foundStart = true;
                                } else if (line.contains(endElement)) {
                                    foundEnd = true;
                                    break;
                                } else if (foundStart && !foundEnd) {
                                    paragraph += line + "\n";
                                }
                            }
                            articleData.setFull_text(paragraph);
                        } catch (Exception e) {
                            continue;
                        }
                    }

                    articleData.setJournal(journal);

                    Article article2 = articleRepo.save(articleData);

                    Elements creator = dc.get(0).getElementsByTag("dc:creator");

                    if (!creator.isEmpty()) {
                        for (int x = 0; x < creator.size(); x++) {
                            Author author = new Author();
                            author.setFirst_name(creator.get(x).text().split(", ")[1]);
                            author.setLast_name(creator.get(x).text().split(", ")[0]);
                            author.setArticle(article2);

                            authorRepo.save(author);
                        }

                        if (subjectsData != null) {
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

                        System.out.println(article2);
                    }
                } else {
                    if (article.get().getLast_modifier() != articleData.getLast_modifier()) {

                        if (articleData.getArticle_pdf() != null
                                && !articleData.getArticle_pdf().contains("downloadSuppFile")
                                && !articleData.getArticle_pdf()
                                        .contains("info")) {
                            try {
                                URL url = new URL(articleData.getArticle_pdf());
                                InputStream inputStream = url.openStream();
                                PDDocument document = PDDocument.load(inputStream);
                                if (document.isEncrypted()) {
                                    document.setAllSecurityToBeRemoved(false);
                                }
                                PDFTextStripper stripper = new PDFTextStripper();
                                String text = stripper.getText(document);
                                document.close();

                                String[] lines = text.split("\n");
                                String paragraph = "";
                                String startElement = "1. INTRODUCTION";
                                String endElement = "BIOGRAPHIES OF AUTHORS";
                                boolean foundStart = false;
                                boolean foundEnd = false;
                                for (String line : lines) {
                                    if (line.contains(startElement)) {
                                        foundStart = true;
                                    } else if (line.contains(endElement)) {
                                        foundEnd = true;
                                        break;
                                    } else if (foundStart && !foundEnd) {
                                        paragraph += line + "\n";
                                    }
                                }
                                articleData.setFull_text(paragraph);
                            } catch (Exception e) {
                                continue;
                            }
                        }

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
                        article.get().setFull_text(articleData.getFull_text());
                        article.get().setJournal(journal);

                        Elements creator = dc.get(0).getElementsByTag("dc:creator");

                        if (!creator.isEmpty()) {
                            for (int x = 0; x < creator.size(); x++) {
                                Author author = new Author();
                                author.setFirst_name(creator.get(x).text().split(", ")[1]);
                                author.setLast_name(creator.get(x).text().split(", ")[0]);
                                author.setArticle(article.get());

                                authorRepo.save(author);
                            }

                            if (subjectsData != null) {
                                for (int t = 0; t < subjectsData.split(";").length; t++) {
                                    if (subjectsData.split(";")[t].trim() != null
                                            && !subjectsData.split(";")[t].trim().isEmpty()) {
                                        Subject subjectRes = new Subject();
                                        subjectRes.setName(subjectsData.split(";")[t].trim());

                                        Optional<Subject> subjectOptional = subjectRepo
                                                .findByName(subjectRes.getName());

                                        if (subjectOptional.isPresent()) {
                                            subjectOptional.get().getArticles().add(article.get());
                                            subjectRepo.save(subjectOptional.get());
                                        }
                                    }
                                }
                            }

                            System.out.println(article.get());
                        }

                    }
                }
            }
        }

    }

    @Scheduled(cron = "0 0 0 18 * *", zone = "GMT+7")
    private <T> void articleCitationScopus() {
        Pageable pageable = PageRequest.of(0, 50);

        Page<Article> articles = articleRepo.findAll(pageable);

        RestTemplate restTemplate = new RestTemplate();

        for (int i = 0; i < articles.getTotalPages(); i++) {
            if (articles.getContent().size() > 0) {
                for (Article article : articles.getContent()) {
                    if (article.getDoi() != null && article.getDoi().split("http").length != 2) {
                        try {
                            HttpHeaders headers = new HttpHeaders();
                            headers.set("X-ELS-APIKey", "bb0f9584e36074a974a78c90396f08f5");

                            HttpEntity<T> request = new HttpEntity<>(headers);

                            ResponseEntity<ExampleCitationScopus> result = restTemplate.exchange(
                                    "https://api.elsevier.com/content/search/scopus?query=DOI(" + article.getDoi()
                                            + ")",
                                    HttpMethod.GET,
                                    request,
                                    new ParameterizedTypeReference<ExampleCitationScopus>() {
                                    });

                            if (result.getStatusCode().value() == 200) {
                                ExampleCitationScopus data = result.getBody();

                                if (data != null) {
                                    SearchResults searchResults = data.getSearchResults();

                                    if (!searchResults.getEntry().isEmpty()
                                            && searchResults.getEntry().get(0).getCitedbyCount() != null) {
                                        List<EntryScopus> entryScopus = searchResults.getEntry();

                                        if (!entryScopus.isEmpty()) {
                                            Optional<CitationScopus> citationScopus = citationScopusRepo
                                                    .findByArticleId(article.getId());

                                            if (!citationScopus.isPresent()) {
                                                CitationScopus citationScopus2 = new CitationScopus();
                                                citationScopus2.setArticle(article);
                                                citationScopus2
                                                        .setReferences_count(
                                                                Integer.valueOf(entryScopus.get(0).getCitedbyCount()));

                                                citationScopusRepo.save(citationScopus2);
                                                System.out.println(citationScopus2);
                                            }

                                            citationScopus.get().setArticle(article);
                                            citationScopus.get()
                                                    .setReferences_count(
                                                            Integer.valueOf(entryScopus.get(0).getCitedbyCount()));

                                            citationScopusRepo.save(citationScopus.get());

                                            System.out.println(citationScopus.get());
                                        }

                                    }

                                }

                            }

                        } catch (Exception e) {
                            continue;
                        }

                    }

                }
            }
            pageable = articles.nextPageable();
            articles = articleRepo.findAll(pageable);
        }

    }

    @Scheduled(cron = "0 0 0 21 * *", zone = "GMT+7")
    private <T> void articleCitationCrossRef() {
        Pageable pageable = PageRequest.of(0, 50);

        Page<Article> articles = articleRepo.findAll(pageable);

        RestTemplate restTemplate = new RestTemplate();

        for (int i = 0; i < articles.getTotalPages(); i++) {

            if (articles.getContent().size() > 0) {
                for (Article article : articles.getContent()) {
                    if (article.getDoi() != null && article.getDoi().split("http").length != 2) {
                        try {

                            ResponseEntity<ExampleCitationCrossRef> result = restTemplate.exchange(
                                    "https://api.crossref.org/works/" + article.getDoi(),
                                    HttpMethod.GET,
                                    null,
                                    ExampleCitationCrossRef.class);

                            if (result.getStatusCode().value() == 200) {
                                ExampleCitationCrossRef body = result.getBody();

                                if (body != null) {
                                    EntryCrossRef entryCrossRef = body.getMessage();

                                    Optional<CitationCrossRef> citationCrossRef = citationCrossRefRepo
                                            .findByArticleId(article.getId());

                                    if (!citationCrossRef.isPresent()) {
                                        CitationCrossRef citationCrossRef2 = new CitationCrossRef();
                                        citationCrossRef2.setArticle(article);
                                        citationCrossRef2.setReferences_count(entryCrossRef.getReferencesCount());

                                        citationCrossRefRepo.save(citationCrossRef2);
                                        System.out.println(citationCrossRef2);
                                    }

                                    citationCrossRef.get().setArticle(article);
                                    citationCrossRef.get().setReferences_count(entryCrossRef.getReferencesCount());

                                    citationCrossRefRepo.save(citationCrossRef.get());
                                    System.out.println(citationCrossRef.get());
                                }

                            }

                        } catch (Exception e) {
                            continue;
                        }

                    }

                }
            }

            pageable = articles.nextPageable();
            articles = articleRepo.findAll(pageable);
        }

    }

    @Scheduled(cron = "0 0 0 25 * *", zone = "GMT+7")
    public void getFigures() {
        Pageable pageable = PageRequest.of(0, 50);

        Page<Article> articles = articleRepo.findAll(pageable);

        for (int i = 0; i < articles.getTotalPages(); i++) {

            if (articles.getContent().size() > 0) {
                for (Article article : articles.getContent()) {
                    if (article.getArticle_pdf() != null
                            && !article.getArticle_pdf().contains("downloadSuppFile") && !article.getArticle_pdf()
                                    .contains("info")) {

                        try {
                            URL url = new URL(article.getArticle_pdf());
                            InputStream inputStream = url.openStream();

                            PDDocument document = PDDocument.load(inputStream);

                            int counter = 0;
                            for (PDPage page : document.getPages()) {
                                PDResources resources = page.getResources();
                                for (COSName name : resources.getXObjectNames()) {
                                    PDXObject xobject = resources.getXObject(name);
                                    if (xobject instanceof PDImageXObject) {
                                        PDImageXObject image = (PDImageXObject) xobject;
                                        BufferedImage bImage = image.getImage();

                                        ByteArrayOutputStream dataStream = new ByteArrayOutputStream();
                                        ImageIO.write(bImage, "png", dataStream);
                                        dataStream.flush();

                                        byte[] imageInByteArray = dataStream.toByteArray();

                                        try {
                                            UriComponents uriComponents = UriComponentsBuilder.newInstance()
                                                    .scheme("http")
                                                    .host("localhost:8080")
                                                    .path("/api/upload/figure/" + article.getTitle()
                                                            .replaceAll(" ", "-")
                                                            + counter + ".png")
                                                    .build();

                                            if (counter == 1) {
                                                FileOutputStream fos = new FileOutputStream("upload/figure/"
                                                        + article.getTitle()
                                                                .replaceAll(" ", "-")
                                                        + counter + ".png");
                                                fos.write(imageInByteArray);
                                                fos.close();
                                                article.setThumbnail(uriComponents.toString());

                                                articleRepo.save(article);
                                                System.out.println(article);
                                            }
                                            counter++;

                                        } catch (Exception e) {
                                            continue;
                                        }

                                    }
                                }
                            }

                            document.close();
                        } catch (Exception e) {
                            continue;
                        }

                    }
                }
            }

            pageable = articles.nextPageable();
            articles = articleRepo.findAll(pageable);
        }

    }

    @Scheduled(cron = "0 0 0 28 * *", zone = "GMT+7")
    public void getFilePDF() {
        Pageable pageable = PageRequest.of(0, 50);

        Page<Article> articles = articleRepo.findAll(pageable);

        for (int i = 0; i < articles.getTotalPages(); i++) {

            if (articles.getContent().size() > 0) {
                for (Article article : articles.getContent()) {
                    if (article.getArticle_pdf() != null
                            && !article.getArticle_pdf().contains("downloadSuppFile") && !article.getArticle_pdf()
                                    .contains("info")) {

                        try {
                            URL url = new URL(article.getArticle_pdf());

                            InputStream in = new BufferedInputStream(url.openStream());
                            ByteArrayOutputStream out = new ByteArrayOutputStream();
                            byte[] buf = new byte[1024];
                            int n = 0;
                            while (-1 != (n = in.read(buf))) {
                                out.write(buf, 0, n);
                            }
                            out.close();
                            in.close();
                            byte[] response = out.toByteArray();

                            FileOutputStream fos = new FileOutputStream(
                                    "upload/document/" + article.getTitle().replaceAll(" ",
                                            "-") + ".pdf");

                            try {
                                fos.write(response);
                                fos.close();
                            } catch (Exception e) {
                                continue;
                            }

                            UriComponents uriComponents = UriComponentsBuilder.newInstance()
                                    .scheme("http")
                                    .host("localhost:8080")
                                    .path("/api/upload/document/" + article.getTitle().replaceAll(" ",
                                            "-")
                                            + ".pdf")
                                    .build();

                            article.setArticle_pdf(uriComponents.toString());
                            articleRepo.save(article);
                            System.out.println(article);
                        } catch (Exception e) {
                            continue;
                        }

                    }
                }
            }

            pageable = articles.nextPageable();
            articles = articleRepo.findAll(pageable);
        }

    }

    @Scheduled(cron = "0 0 0 1 * *", zone = "GMT+7")
    public void pushData() {
        Pageable pageable = PageRequest.of(0, 50);

        Page<Article> articles = articleRepo.findAll(pageable);

        for (int i = 0; i < articles.getTotalPages(); i++) {

            if (articles.getContent().size() > 0) {
                for (Article article : articles.getContent()) {
                    ArticleElastic articleElastic = new ArticleElastic();

                    JournalElastic journalElastic = new JournalElastic();
                    journalElastic.setId(article.getJournal().getId().toString());
                    journalElastic.setName(article.getJournal().getName());
                    journalElastic.setIssn(article.getJournal().getIssn());
                    journalElastic.setAbbreviation(article.getJournal().getAbbreviation());
                    journalElastic.setThumbnail(article.getJournal().getThumbnail());
                    journalElastic.setDescription(article.getJournal().getDescription());
                    journalElastic.setPublisher(article.getJournal().getPublisher());
                    journalElastic.setJournal_site(article.getJournal().getJournal_site());
                    journalElastic.setFrequency(article.getJournal().getFrequency());
                    journalElastic.setCountry(article.getJournal().getCountry());
                    journalElastic.setAim_scope_site(article.getJournal().getAim_scope_site());
                    journalElastic.setIntroduction_author_site(article.getJournal().getIntroduction_author_site());
                    journalElastic.setHost_platform(article.getJournal().getHost_platform());
                    journalElastic.setIssue_per_year(article.getJournal().getIssue_per_year());
                    journalElastic.setPrimary_languange(article.getJournal().getPrimary_languange());
                    journalElastic.setEditor_site(article.getJournal().getEditor_site());
                    journalElastic.setFull_text_format(article.getJournal().getFull_text_format());
                    journalElastic.setArticle_doi(article.getJournal().isArticle_doi());
                    journalElastic.setStatement(article.getJournal().getStatement());
                    journalElastic.setLicense(article.getJournal().getLicense());
                    journalElastic.setApc_fee(article.getJournal().getApc_fee());
                    journalElastic.setReview_police(article.getJournal().getReview_police());
                    journalElastic.setMetric(article.getJournal().getMetric());
                    journalElastic.setUpdatedAt(new Date(article.getupdatedAt().getTime()));
                    journalElastic.setCreatedAt(new Date(article.getcreatedAt().getTime()));

                    articleElastic.setId(article.getId().toString());
                    articleElastic.setJournal(journalElastic);
                    articleElastic.setThumbnail(article.getThumbnail());
                    articleElastic.setOjs_id(article.getOjs_id());
                    articleElastic.setSet_spec(article.getSet_spec());
                    articleElastic.setSubjects(article.getSubjects());
                    articleElastic.setTitle(article.getTitle());
                    articleElastic.setPages(article.getPages());
                    articleElastic.setPublisher(article.getPublisher());
                    articleElastic.setPublish_date(article.getPublish_date());
                    articleElastic.setPublish_year(article.getPublish_year());
                    articleElastic.setLast_modifier(article.getLast_modifier());
                    articleElastic.setIssn(article.getIssn());
                    articleElastic.setSource_type(article.getSource_type());
                    articleElastic.setLanguange_publication(article.getLanguange_publication());
                    articleElastic.setDoi(article.getDoi());
                    articleElastic.setVolume(article.getVolume());
                    articleElastic.setIssue(article.getIssue());
                    articleElastic.setCopyright(article.getCopyright());
                    articleElastic.setAbstract_text(article.getAbstract_text());
                    articleElastic.setFull_text(article.getFull_text());
                    articleElastic.setArticle_pdf(article.getArticle_pdf());
                    articleElastic.setKeyword(article.getKeyword());
                    articleElastic.setUpdatedAt(
                            new Date(article.getupdatedAt().getTime()));
                    articleElastic.setCreatedAt(new Date(article.getcreatedAt().getTime()));
                    articleElastic.setCitation_by_scopus(article.getCitation_by_scopus());
                    articleElastic.setCitation_by_cross_ref(article.getCitation_by_cross_ref());

                    IndexResponse articleElastic2 = elasticRepo.save(articleElastic);
                    System.out.println(articleElastic2);
                }
            }

            pageable = articles.nextPageable();
            articles = articleRepo.findAll(pageable);
        }

    }

}
