package com.ipmugo.library.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ipmugo.library.data.Subject;
import com.ipmugo.library.repository.SubjectRepo;

@Service
public class SubjectService {

    @Autowired
    private SubjectRepo subjectRepo;

    public List<Subject> findAll() {
        return subjectRepo.findAll();
    }

    public Subject findOne(UUID id) {
        Optional<Subject> category = subjectRepo.findById(id);

        if (!category.isPresent()) {
            return null;
        }

        return category.get();
    }

    public Subject save(Subject category) {
        return subjectRepo.save(category);
    }

    public void deleteById(UUID id) {
        subjectRepo.deleteById(id);
    }

    public Subject findByName(String name) {
        Optional<Subject> category = subjectRepo.findByName(name);

        if (!category.isPresent()) {
            return null;
        }

        return category.get();
    }
}
