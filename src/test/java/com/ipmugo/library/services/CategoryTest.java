package com.ipmugo.library.services;

import static org.mockito.Mockito.times;

import java.util.Collections;
import java.util.List;
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
import com.ipmugo.library.repository.CategoryRepo;
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
    void testFindByIdFail() {
        Mockito.when(categoryRepo.findById(UUID_ID))
                .thenReturn(Optional.empty());

        var category = categoryService.findOne(UUID_ID);

        Assertions.assertThat(category).isNull();

    }

    @Test
    void testFindByIdSuccess() {
        Mockito.when(categoryRepo.findById(UUID_ID))
                .thenReturn(Optional.of(new Category(UUID_ID, "Computer")));

        var category = categoryService.findOne(UUID_ID);
        Assertions.assertThat(category).isNotNull();
        Assertions.assertThat(category.getName()).isEqualTo("Computer");

    }

    @Test
    void testFindByNameFail() {
        Mockito.when(categoryRepo.findByName("Computer"))
                .thenReturn(Optional.empty());

        var category = categoryService.findByName("Computer");

        Assertions.assertThat(category).isNull();

    }

    void testFindByNameSuccess() {
        Mockito.when(categoryRepo.findByName("Computer"))
                .thenReturn(Optional.of(new Category(UUID_ID, "Computer")));

        var category = categoryService.findByName("Computer");
        Assertions.assertThat(category).isNotNull();
        Assertions.assertThat(category.getName()).isEqualTo("Computer");

    }

    @Test
    void testFindAllEmpty() {
        Mockito.when(categoryRepo.findAll()).thenReturn(Collections.emptyList());

        var category = categoryService.findAll();

        Assertions.assertThat(category).isEmpty();
    }

    @Test
    void testFindAll() {
        Mockito.when(categoryRepo.findAll()).thenReturn(List.of(new Category(UUID_ID, "Computer")));

        var category = categoryService.findAll();

        Assertions.assertThat(category).isNotEmpty();
        Assertions.assertThat(category.size()).isEqualTo(1);
    }

    @Test
    void testDeleteCategory() {
        Category category = new Category(UUID_ID, "Computer");
        categoryService.deleteById(category.getId());
    }

    @Test
    void testSaveCategorySuccess() {
        Category category = new Category(UUID_ID, "Computer");

        Mockito.when(categoryRepo.save(category)).thenReturn(category);

        var cat = categoryService.save(category);

        Assertions.assertThat(cat).isNotNull();
        Assertions.assertThat(cat.getName()).isEqualTo("Computer");

        Mockito.verify(categoryRepo, times(1)).save(category);

    }

    @Test
    void testSaveCategoryFail() {
        Category category = new Category(UUID_ID, "Computer");

        Mockito.when(categoryRepo.save(category)).thenReturn(null);

        var cat = categoryService.save(category);

        Assertions.assertThat(cat).isNull();

        Mockito.verify(categoryRepo, times(1)).save(category);

    }

    @Test
    void testSaveCategoryError() {
        Category category = new Category(UUID_ID, "Computer");

        categoryService.save(category);

        Assertions.assertThatException();

        Mockito.verify(categoryRepo, times(1)).save(category);

    }
}
