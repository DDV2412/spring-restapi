package com.ipmugo.library.repository;

import static org.mockito.Mockito.times;

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

import com.ipmugo.library.data.Category;
import com.ipmugo.library.service.CategoryService;

@Extensions({
        @ExtendWith(MockitoExtension.class)
})
public class CategoryTest {

    final static public UUID UUID_ID = UUID.fromString("a068e4f7-9760-4881-9114-30b62cd2a672");

    @Mock
    private CategoryRepo categoryRepo;

    @InjectMocks
    private CategoryService categoryService;

    @Test
    void testGetCategoryFail() {
        Mockito.when(categoryRepo.findById(UUID_ID))
                .thenReturn(Optional.empty());

        var category = categoryService.findOne(UUID_ID);

        Mockito.verify(categoryRepo, times(1)).findById(UUID_ID);
        Assertions.assertThat(category).isNull();

    }

    @Test
    void testGetCategorySuccess() {
        Mockito.when(categoryRepo.findById(UUID_ID))
                .thenReturn(Optional.of(new Category(UUID_ID, "Computer")));

        var category = categoryService.findOne(UUID_ID);
        Mockito.verify(categoryRepo, times(1)).findById(UUID_ID);
        Assertions.assertThat(category).isNotNull();

    }
}
