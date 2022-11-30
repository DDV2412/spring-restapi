package com.ipmugo.library.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ipmugo.library.data.Journal;
import com.ipmugo.library.repository.JournalRepo;

import jakarta.transaction.TransactionScoped;

@Service
@TransactionScoped
public class JournalService {

    @Autowired
    private JournalRepo journalRepo;

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
}
