package com.ipmugo.library.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ipmugo.library.data.AuthorCitation;
import com.ipmugo.library.repository.AuthorCitationRepo;

@Service
public class AuthorCitationService {

    @Autowired
    private AuthorCitationRepo authorCitationRepo;

    public AuthorCitation save(AuthorCitation authorCitation) {
        return authorCitationRepo.save(authorCitation);
    }
}
