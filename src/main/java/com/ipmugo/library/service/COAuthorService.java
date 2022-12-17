package com.ipmugo.library.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ipmugo.library.data.COAuthor;
import com.ipmugo.library.repository.COAuthorRepo;

@Service
public class COAuthorService {

    @Autowired
    private COAuthorRepo coAuthorRepo;

    public COAuthor save(COAuthor coAuthor) {
        return coAuthorRepo.save(coAuthor);
    }
}
