package com.ipmugo.library.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ipmugo.library.data.Category;
import com.ipmugo.library.repository.CategoryRepo;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepo categoryRepo;

    public List<Category> findAll() {
        return categoryRepo.findAll();
    }

    public Category findOne(UUID id) {
        Optional<Category> category = categoryRepo.findById(id);

        if (!category.isPresent()) {
            return null;
        }

        return category.get();
    }

    public Category save(Category category) {
        return categoryRepo.save(category);
    }

    public void deleteById(UUID id) {
        categoryRepo.deleteById(id);
    }

    public Category findByName(String name) {
        Optional<Category> category = categoryRepo.findByName(name);

        if (!category.isPresent()) {
            return null;
        }

        return category.get();
    }
}
