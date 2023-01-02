package com.ipmugo.library.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.ipmugo.library.data.Author;
import com.ipmugo.library.repository.AuthorRepo;

import jakarta.transaction.TransactionScoped;

@Service
@TransactionScoped
public class AuthorService {

    @Autowired
    private AuthorRepo authorRepo;

    public Page<Author> findAll(Pageable pageable) {
        return authorRepo.findAll(pageable);
    }

    public List<Author> findByArticleId(UUID articleId) {
        return authorRepo.findByArticleId(articleId);
    }

    public Author findOne(UUID id) {
        Optional<Author> author = authorRepo.findById(id);

        if (!author.isPresent()) {
            return null;
        }

        return author.get();
    }

    public Author save(Author author) {
        return authorRepo.save(author);
    }

    public Iterable<Author> saveAll(List<Author> authors) {
        return authorRepo.saveAll(authors);
    }

    public void deleteById(UUID id) {
        authorRepo.deleteById(id);
    }

}
