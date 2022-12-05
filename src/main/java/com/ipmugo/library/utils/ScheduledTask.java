package com.ipmugo.library.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;
import com.ipmugo.library.data.Article;
import com.ipmugo.library.data.Author;
import com.ipmugo.library.data.Journal;
import com.ipmugo.library.data.Subject;
import com.ipmugo.library.service.ArticleService;
import com.ipmugo.library.service.AuthorService;
import com.ipmugo.library.service.JournalService;
import com.ipmugo.library.service.SubjectService;

@Component
@EnableAsync
public class ScheduledTask {

    @Autowired
    private JournalService journalService;

    @Autowired
    private SubjectService subjectService;

    @Autowired
    private ArticleService articleService;

    @Autowired
    private AuthorService authorService;

    @Async
    @Scheduled(cron = "* * * 6 * *")
    public void getHarvest() {
        try {
            List<Article> articles = new ArrayList<>();

            List<Journal> journals = journalService.findAll();

            if (journals.size() == 0) {
                System.out.println("Journal recorded is empty");
            }

            for (int i = 0; i < journals.size(); i++) {
                Journal journal = journals.get(i);

                Response response = Jsoup.connect(
                        journal.getJournal_site() +
                                "/oai?verb=ListRecords&metadataPrefix=oai_dc&set=" + journal.getAbbreviation())
                        .timeout(0)
                        .execute();
                Document document = Jsoup.connect(
                        journal.getJournal_site() +
                                "/oai?verb=ListRecords&metadataPrefix=oai_dc&set=" + journal
                                        .getAbbreviation())
                        .timeout(0)
                        .get();

                if (response.statusCode() != 200 || !document.getElementsByTag("error").isEmpty()) {
                    Logger.getLogger(
                            ScheduledTask.class.getName()).log(Level.SEVERE,
                                    document.getElementsByTag("error").first().text());
                }

                articles.addAll(parseData(document));

                if (!document.getElementsByTag("resumptionToken").isEmpty()) {
                    articles.addAll(
                            resumptionToken(journal, document.getElementsByTag("resumptionToken").first().text()));
                }

                System.out.println(new Gson().toJson(articles));
            }

        } catch (Exception ex) {
            Logger.getLogger(ScheduledTask.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private List<Article> resumptionToken(Journal journal, String resumptionToken) {
        try {
            List<Article> articles = new ArrayList<>();

            Response response = Jsoup.connect(
                    journal.getJournal_site() +
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

            if (response.statusCode() != 200 || !document.getElementsByTag("error").isEmpty()) {
                Logger.getLogger(
                        ScheduledTask.class.getName()).log(Level.SEVERE,
                                document.getElementsByTag("error").first().text());
            }

            articles.addAll(parseData(document));

            if (!document.getElementsByTag("resumptionToken").isEmpty()) {
                articles.addAll(resumptionToken(journal, document.getElementsByTag("resumptionToken").first().text()));
            }

            return articles;

        } catch (Exception ex) {
            return null;
        }
    }

    private List<Article> parseData(Document records) {

        List<Article> articles = new ArrayList<>();

        Elements recordData = records.getElementsByTag("record");

        for (int i = 0; i < recordData.size(); i++) {
            if (!recordData.get(i).getElementsByTag("header").get(0).attr("status").isEmpty()) {
                continue;
            }

            if (recordData.get(i).getElementsByTag("metadata").isEmpty()) {
                continue;
            }

            if (recordData.get(i).getElementsByTag("oai_dc:dc").isEmpty()) {
                continue;
            }

            Elements header = recordData.get(i).getElementsByTag("header");

            String identifier = header.first().getElementsByTag("identifier").text().split("/")[1];

            String last_modified = header.first().getElementsByTag("datestamp").text();

            String set_spec = header.first().getElementsByTag("setSpec").text().split(":")[1];

            Elements metadata = recordData.get(i).getElementsByTag("metadata").first().getElementsByTag("oai_dc:dc");

            String title = metadata.first().getElementsByTag("dc:title").text();

            String publisher = metadata.first().getElementsByTag("dc:publisher").text();

            String publish_date = metadata.first().getElementsByTag("dc:date").text();

            String publish_year = publish_date.split("-")[0];

            String source_type = metadata.first().getElementsByTag("dc:type").first().text();

            String languange_publisher = metadata.first().getElementsByTag("dc:language").text();

            Elements sources = metadata.first().getElementsByTag("dc:source");

            String issn = sources.get(2).text();

            String volume = null;

            String issue = null;

            String pages = null;

            String abstract_text = null;

            if (!metadata.first().getElementsByTag("dc:description").isEmpty()) {
                abstract_text = metadata.first().getElementsByTag("dc:description").text();
            }

            String doi = null;

            if (metadata.first().getElementsByTag("dc:identifier").size() == 2) {
                doi = metadata.first().getElementsByTag("dc:identifier").last().text();
            }

            if (abstract_text != null) {
                if (abstract_text.split("DOI:").length > 2) {
                    abstract_text = abstract_text.split("DOI:")[0];

                    if (abstract_text.split("DOI:")[1].isEmpty() && abstract_text.split("DOI:")[1] == null) {
                        System.out.println();
                    } else {
                        if (abstract_text.split("DOI:")[1].split("doi.org/").length > 2) {
                            doi = abstract_text.split("DOI:")[1].split("doi.org/")[1];
                        } else {
                            doi = abstract_text.split("DOI:")[1].split("doi.org/")[0];
                        }
                    }
                }
            }

            if (sources.first().text().split(";").length == 3) {
                volume = sources.first().text().split(";")[1].split(",")[0].split("Vol ")[1];

                issue = sources.first().text().split(";")[1].split(",")[1].split(":")[0].split("No ")[1];

                pages = sources.first().text().split(";")[2];
            }

            String article_pdf = null;

            String keyword = null;

            String subjectsData = null;

            Elements subject = metadata.get(0).getElementsByTag("dc:subject");

            if (subject.size() == 2) {
                if (!subject.isEmpty()) {
                    subjectsData = subject.get(0).text();

                    keyword = subject.get(1).text();
                }
            } else {
                if (!subject.isEmpty()) {
                    keyword = subject.first().text();
                }
            }

            if (!metadata.first().getElementsByTag("dc:relation").isEmpty()) {
                article_pdf = metadata.first().getElementsByTag("dc:relation").text().replaceAll("/view/",
                        "/download/");
            }

            String copyright = null;

            if (metadata.first().getElementsByTag("dc:rights").size() > 1) {
                copyright = metadata.first().getElementsByTag("dc:rights").last().text();
            }

            List<Subject> subjects = new ArrayList<>();

            if (subjectsData != null) {
                subjectsData = subjectsData.replaceAll("; ", ";");
                subjectsData = subjectsData.replaceAll(", ", ";");
                subjectsData = subjectsData.replaceAll(",", ";");

                for (int t = 0; t < subjectsData.split(";").length; t++) {
                    Subject subjectResult = subjectService.findByName(subjectsData.split(";")[t]);

                    if (subjectResult == null) {
                        Subject newValue = new Subject();
                        newValue.setName(subjectsData.split(";")[t]);

                        subjects.add(subjectService.save(newValue));
                    } else {
                        subjects.add(subjectResult);
                    }
                }
            }

            Elements creator = metadata.get(0).getElementsByTag("dc:creator");

            Article articleData = new Article();

            if (doi != null) {
                Article checkData = articleService.findByDoi(doi);

                if (checkData == null) {
                    if (subjects != null && articleData.getSubjects() != null) {
                        articleData.setOjs_id(Integer.parseInt(identifier));
                        articleData.setLast_modifier(last_modified);
                        articleData.setSet_spec(set_spec);
                        articleData.setTitle(title);
                        articleData.setAbstract_text(abstract_text);
                        articleData.setPublisher(publisher);
                        articleData.setPublish_date(publish_date);
                        articleData.setPublish_year(publish_year);
                        articleData.setSource_type(source_type);
                        articleData.setDoi(doi);
                        articleData.setLanguange_publication(languange_publisher);
                        articleData.setArticle_pdf(article_pdf);
                        articleData.setCopyright(copyright);
                        articleData.setIssn(issn);
                        articleData.setIssue(issue);
                        articleData.setVolume(volume);
                        articleData.setPages(pages);
                        articleData.setKeyword(keyword);
                        articleData.getSubjects().addAll(subjects);

                        Article articleResult = articleService.save(articleData);

                        for (int x = 0; x < creator.size(); x++) {
                            Author author = new Author();
                            author.setFirst_name(creator.get(x).text().split(", ")[1]);
                            author.setLast_name(creator.get(x).text().split(", ")[0]);
                            author.setArticle(articleResult);

                            authorService.save(author);
                        }
                    }

                    articleData.setOjs_id(Integer.parseInt(identifier));
                    articleData.setLast_modifier(last_modified);
                    articleData.setSet_spec(set_spec);
                    articleData.setTitle(title);
                    articleData.setAbstract_text(abstract_text);
                    articleData.setPublisher(publisher);
                    articleData.setPublish_date(publish_date);
                    articleData.setPublish_year(publish_year);
                    articleData.setSource_type(source_type);
                    articleData.setDoi(doi);
                    articleData.setLanguange_publication(languange_publisher);
                    articleData.setArticle_pdf(article_pdf);
                    articleData.setCopyright(copyright);
                    articleData.setIssn(issn);
                    articleData.setIssue(issue);
                    articleData.setVolume(volume);
                    articleData.setPages(pages);
                    articleData.setKeyword(keyword);

                    Article articleResult = articleService.save(articleData);

                    for (int x = 0; x < creator.size(); x++) {
                        Author author = new Author();
                        author.setFirst_name(creator.get(x).text().split(", ")[1]);
                        author.setLast_name(creator.get(x).text().split(", ")[0]);
                        author.setArticle(articleResult);

                        authorService.save(author);
                    }

                } else {
                    if (subjects != null && articleData.getSubjects() != null) {
                        checkData.setOjs_id(Integer.parseInt(identifier));
                        checkData.setLast_modifier(last_modified);
                        checkData.setSet_spec(set_spec);
                        checkData.setTitle(title);
                        checkData.setAbstract_text(abstract_text);
                        checkData.setPublisher(publisher);
                        checkData.setPublish_date(publish_date);
                        checkData.setPublish_year(publish_year);
                        checkData.setSource_type(source_type);
                        checkData.setDoi(doi);
                        checkData.setLanguange_publication(languange_publisher);
                        checkData.setArticle_pdf(article_pdf);
                        checkData.setCopyright(copyright);
                        checkData.setIssn(issn);
                        checkData.setIssue(issue);
                        checkData.setVolume(volume);
                        checkData.setPages(pages);
                        checkData.setKeyword(keyword);
                        checkData.getSubjects().addAll(subjects);

                        Article articleResult = articleService.save(checkData);

                        for (int x = 0; x < creator.size(); x++) {
                            Author author = new Author();
                            author.setFirst_name(creator.get(x).text().split(", ")[1]);
                            author.setLast_name(creator.get(x).text().split(", ")[0]);
                            author.setArticle(articleResult);

                            authorService.save(author);
                        }
                    }

                    checkData.setOjs_id(Integer.parseInt(identifier));
                    checkData.setLast_modifier(last_modified);
                    checkData.setSet_spec(set_spec);
                    checkData.setTitle(title);
                    checkData.setAbstract_text(abstract_text);
                    checkData.setPublisher(publisher);
                    checkData.setPublish_date(publish_date);
                    checkData.setPublish_year(publish_year);
                    checkData.setSource_type(source_type);
                    checkData.setDoi(doi);
                    checkData.setLanguange_publication(languange_publisher);
                    checkData.setArticle_pdf(article_pdf);
                    checkData.setCopyright(copyright);
                    checkData.setIssn(issn);
                    checkData.setIssue(issue);
                    checkData.setVolume(volume);
                    checkData.setPages(pages);
                    checkData.setKeyword(keyword);

                    Article articleResult = articleService.save(checkData);

                    for (int x = 0; x < creator.size(); x++) {
                        Author author = new Author();
                        author.setFirst_name(creator.get(x).text().split(", ")[1]);
                        author.setLast_name(creator.get(x).text().split(", ")[0]);
                        author.setArticle(articleResult);

                        authorService.save(author);
                    }
                }
            }
        }

        return articles;
    }
}
