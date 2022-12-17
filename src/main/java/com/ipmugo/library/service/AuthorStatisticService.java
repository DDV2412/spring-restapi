package com.ipmugo.library.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ipmugo.library.data.AuthorStatistic;
import com.ipmugo.library.repository.AuthorStatisticRepo;

@Service
public class AuthorStatisticService {

    @Autowired
    private AuthorStatisticRepo authorStatisticRepo;

    public AuthorStatistic save(AuthorStatistic authorStatistic) {
        return authorStatisticRepo.save(authorStatistic);
    }
}
