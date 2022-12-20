package com.ipmugo.library.controllers;

import java.util.ArrayList;
import java.util.UUID;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ipmugo.library.data.Category;
import com.ipmugo.library.data.Journal;
import com.ipmugo.library.data.Metric;
import com.ipmugo.library.dto.CategoryData;
import com.ipmugo.library.dto.JournalData;
import com.ipmugo.library.dto.ResponseData;
import com.ipmugo.library.dto.ResponseDataWithCount;
import com.ipmugo.library.service.CategoryService;
import com.ipmugo.library.service.JournalService;
import com.ipmugo.library.utils.Frequency;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/journal")
public class JournalController {

    @Autowired
    private JournalService journalService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private ModelMapper modelMapper;

    @GetMapping
    public ResponseEntity<ResponseDataWithCount<Iterable<Journal>>> findAll(@RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "15") int size, @RequestParam(required = false) String name,
            @RequestParam(required = false) String latest, @RequestParam(required = false) String popularity) {
        ResponseDataWithCount<Iterable<Journal>> responseData = new ResponseDataWithCount<>();

        Pageable paging = PageRequest.of(page, size, Sort.by("name").ascending());

        if (popularity != null) {
            if (popularity.equals("asc")) {
                paging = PageRequest.of(page, size, Sort.by("metric.citeScoreCurrent").ascending());
            } else {
                paging = PageRequest.of(page, size, Sort.by("metric.citeScoreCurrent").descending());
            }
        } else if (name != null) {
            if (name.equals("asc")) {
                paging = PageRequest.of(page, size, Sort.by("name").ascending());
            } else {
                paging = PageRequest.of(page, size, Sort.by("name").descending());
            }
        } else if (latest != null) {
            if (latest.equals("asc")) {
                paging = PageRequest.of(page, size, Sort.by("createdAt").ascending());
            } else {
                paging = PageRequest.of(page, size, Sort.by("createdAt").descending());
            }
        }

        Page<Journal> journal = journalService.findAll(paging);

        responseData.setStatus(true);
        responseData.setPayload(journal.getContent());
        responseData.setTotalElements(journal.getTotalElements());
        responseData.setTotalPage(journal.getTotalPages());
        responseData.setCurrentPage(journal.getNumber());
        return ResponseEntity.ok(responseData);

    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseData<Journal>> findById(@PathVariable("id") UUID id) {
        ResponseData<Journal> responseData = new ResponseData<>();

        Journal journal = journalService.findOne(id);

        if (journal == null) {
            responseData.setStatus(false);
            responseData.getMessages().add("Journal with " + id + " not found");

            return ResponseEntity.badRequest().body(responseData);
        }

        responseData.setPayload(journal);
        responseData.setStatus(true);
        responseData.getMessages().add("Successfully get journal by id " + id);

        return ResponseEntity.ok(responseData);

    }

    @GetMapping("/abbreviation/{abbreviation}")
    public ResponseEntity<ResponseData<Journal>> findByAbbreviation(@PathVariable("abbreviation") String abbreviation) {
        ResponseData<Journal> responseData = new ResponseData<>();

        Journal journal = journalService.findByAbbreviation(abbreviation);

        if (journal == null) {
            responseData.setStatus(false);
            responseData.getMessages().add("Journal with " + abbreviation + " not found");

            return ResponseEntity.badRequest().body(responseData);
        }

        responseData.setPayload(journal);
        responseData.setStatus(true);
        responseData.getMessages().add("Successfully get journal by abbreviation " + abbreviation);

        return ResponseEntity.ok(responseData);

    }

    @PostMapping
    public ResponseEntity<ResponseData<Journal>> create(@Valid @RequestBody JournalData journalData, Errors errors) {
        ResponseData<Journal> responseData = new ResponseData<>();

        if (errors.hasErrors()) {
            for (ObjectError error : errors.getAllErrors()) {
                responseData.getMessages().add(error.getDefaultMessage());
            }
            responseData.setStatus(false);
            responseData.setPayload(null);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseData);
        }
        try {
            Journal journal = modelMapper.map(journalData, Journal.class);

            responseData.setStatus(true);
            responseData.setPayload(journalService.save(journal));
            return ResponseEntity.ok(responseData);
        } catch (Exception e) {
            responseData.setStatus(false);
            responseData.getMessages().add("Journal name " + journalData.getName() + " not available");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseData);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseData<Journal>> update(@PathVariable("id") UUID id,
            @Valid @RequestBody JournalData journalData, Errors errors) {
        ResponseData<Journal> responseData = new ResponseData<>();

        if (errors.hasErrors()) {
            for (ObjectError error : errors.getAllErrors()) {
                responseData.getMessages().add(error.getDefaultMessage());
            }
            responseData.setStatus(false);
            responseData.setPayload(null);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseData);
        }
        try {

            Journal journal = journalService.findOne(id);

            if (journal == null) {
                responseData.setStatus(false);
                responseData.setPayload(null);
                responseData.getMessages().add("Journal with " + id + "not found");
                return ResponseEntity.badRequest().body(responseData);
            }

            Journal journalMapper = modelMapper.map(journalData, Journal.class);
            Frequency frequency = modelMapper.map(journalData.getFrequency(), Frequency.class);

            journal.setName(journalMapper.getName());
            journal.setIssn(journalMapper.getIssn());
            journal.setE_issn(journalMapper.getE_issn());
            journal.setAbbreviation(journalMapper.getAbbreviation());
            journal.setDescription(journalMapper.getDescription());
            journal.setThumbnail(journalMapper.getThumbnail());
            journal.setPublisher(journalMapper.getPublisher());
            journal.setJournal_site(journalMapper.getJournal_site());
            journal.setCountry(journalMapper.getCountry());
            journal.setAim_scope_site(journalMapper.getAim_scope_site());
            journal.setIntroduction_author_site(journalMapper.getIntroduction_author_site());
            journal.setHost_platform(journalMapper.getHost_platform());
            journal.setIssue_per_year(journalMapper.getIssue_per_year());
            journal.setPrimary_languange(journalMapper.getPrimary_languange());
            journal.setEditor_site(journalMapper.getEditor_site());
            journal.setFull_text_format(journalMapper.getFull_text_format());
            journal.setArticle_doi(journalMapper.isArticle_doi());
            journal.setFrequency(frequency);
            journal.setStatement(journalMapper.getStatement());
            journal.setLicense(journalMapper.getLicense());
            journal.setApc_fee(journalMapper.getApc_fee());
            journal.setReview_police(journalMapper.getReview_police());

            responseData.setStatus(true);
            responseData.setPayload(journalService.save(journal));
            return ResponseEntity.ok(responseData);
        } catch (Exception e) {
            responseData.setStatus(false);
            responseData.getMessages().add("Journal name " + journalData.getName() + " not available");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseData);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseData<Journal>> deleteById(@PathVariable("id") UUID id) {
        ResponseData<Journal> responseData = new ResponseData<>();

        try {
            journalService.deleteById(id);

            responseData.setStatus(true);
            responseData.getMessages().add("Successfully delete journal by ID" + id);

            return ResponseEntity.ok(responseData);
        } catch (Exception e) {
            responseData.setStatus(false);
            responseData.getMessages().add("Journal by ID " + id + " not exist");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseData);
        }

    }

    @PostMapping("/{id}")
    public ResponseEntity<ResponseData<Journal>> setCategory(@Valid @RequestBody CategoryData categoryData,
            @PathVariable("id") UUID id) {
        ResponseData<Journal> responseData = new ResponseData<>();

        try {
            Category category = categoryService.findOne(categoryData.getId());

            if (category == null) {
                responseData.setStatus(false);
                responseData.getMessages().add("Category not found");

                return ResponseEntity.badRequest().body(responseData);
            }

            responseData.setPayload(journalService.setCategory(id, category));
            responseData.setStatus(true);
            responseData.getMessages().add("Successfully set journal category by ID " + id);

            return ResponseEntity.ok(responseData);
        } catch (Exception e) {
            responseData.setStatus(false);
            responseData.getMessages().add(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseData);
        }
    }

    @PostMapping("/collections/{id}")
    public ResponseEntity<ResponseData<Journal>> setCategories(@Valid @RequestBody ArrayList<Category> categoryData,
            @PathVariable("id") UUID id) {
        ResponseData<Journal> responseData = new ResponseData<>();

        try {
            responseData.setPayload(journalService.setCategories(id, categoryData));
            responseData.setStatus(true);
            responseData.getMessages().add("Successfully set journal category by ID " + id);

            return ResponseEntity.ok(responseData);
        } catch (Exception e) {
            responseData.setStatus(false);
            responseData.getMessages().add(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseData);
        }
    }

    @GetMapping("/citation/{id}")
    public ResponseEntity<ResponseData<Metric>> setCitation(@PathVariable("id") UUID id) {
        ResponseData<Metric> responseData = new ResponseData<>();

        try {
            Journal journal = journalService.findOne(id);

            if (journal == null) {
                responseData.setStatus(false);
                responseData.getMessages().add("Journal with " + id + " not found");

                return ResponseEntity.badRequest().body(responseData);
            }

            Metric journalCitation = journalService.citation(journal);

            if (journalCitation == null) {
                responseData.setStatus(false);
                responseData.getMessages().add("Journal with " + id + " not found");

                return ResponseEntity.badRequest().body(responseData);
            }
            responseData.setPayload(journalCitation);
            responseData.setStatus(true);
            responseData.getMessages().add("Successfully set journal citation by ID " + id);

            return ResponseEntity.ok(responseData);
        } catch (Exception e) {
            responseData.setStatus(false);
            responseData.getMessages().add(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseData);
        }
    }
}
