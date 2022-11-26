package com.ipmugo.library.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ipmugo.library.data.LogSearch;
import com.ipmugo.library.repository.LogSearchRepo;

@Service
public class LogSearchService {

    @Autowired
    private LogSearchRepo logSearchRepo;

    public List<LogSearch> findAll() {
        return logSearchRepo.findAll();
    }

    public LogSearch findOne(UUID id) {
        Optional<LogSearch> logsearch = logSearchRepo.findById(id);

        if (!logsearch.isPresent()) {
            return null;
        }

        return logsearch.get();
    }

    public LogSearch save(LogSearch logsearch) {
        return logSearchRepo.save(logsearch);
    }

    public void deleteById(UUID id) {
        logSearchRepo.deleteById(id);
    }
}
