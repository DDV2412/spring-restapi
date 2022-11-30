package com.ipmugo.library.services;

import java.util.Optional;
import java.util.UUID;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.Extensions;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.ipmugo.library.repository.ArticleRepo;
import com.ipmugo.library.service.ArticleService;

@Extensions({
        @ExtendWith(MockitoExtension.class)
})
public class ArticleTest {

    final static public UUID UUID_ID = UUID.fromString("a068e4f7-9760-4881-9114-30b62cd2a672");

    @Mock
    private ArticleRepo articleRepo;

    @InjectMocks
    private ArticleService articleService;

    @Test
    void testFindByIdFail() {
        Mockito.when(articleRepo.findById(UUID_ID))
                .thenReturn(Optional.empty());

        var article = articleService.findOne(UUID_ID);

        Assertions.assertThat(article).isNull();

    }
}
