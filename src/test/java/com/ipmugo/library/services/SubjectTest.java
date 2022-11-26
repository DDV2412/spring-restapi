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

import com.ipmugo.library.data.Subject;
import com.ipmugo.library.repository.SubjectRepo;
import com.ipmugo.library.service.SubjectService;

@Extensions({
        @ExtendWith(MockitoExtension.class)
})
public class SubjectTest {

    final static public UUID UUID_ID = UUID.fromString("a068e4f7-9760-4881-9114-30b62cd2a672");

    @Mock
    private SubjectRepo subjectRepo;

    @InjectMocks
    private SubjectService subjectService;

    @Test
    void testFindByIdFail() {
        Mockito.when(subjectRepo.findById(UUID_ID))
                .thenReturn(Optional.empty());

        var subject = subjectService.findOne(UUID_ID);

        Assertions.assertThat(subject).isNull();

    }

    @Test
    void testFindByIdSuccess() {
        Mockito.when(subjectRepo.findById(UUID_ID))
                .thenReturn(Optional.of(new Subject(UUID_ID, "Computer")));

        var subject = subjectService.findOne(UUID_ID);
        Assertions.assertThat(subject).isNotNull();
        Assertions.assertThat(subject.getName()).isEqualTo("Computer");

    }

    @Test
    void testFindByNameFail() {
        Mockito.when(subjectRepo.findByName("Computer"))
                .thenReturn(Optional.empty());

        var subject = subjectService.findByName("Computer");

        Assertions.assertThat(subject).isNull();

    }

    void testFindByNameSuccess() {
        Mockito.when(subjectRepo.findByName("Computer"))
                .thenReturn(Optional.of(new Subject(UUID_ID, "Computer")));

        var subject = subjectService.findByName("Computer");
        Assertions.assertThat(subject).isNotNull();
        Assertions.assertThat(subject.getName()).isEqualTo("Computer");

    }

    @Test
    void testFindAllEmpty() {
        Mockito.when(subjectRepo.findAll()).thenReturn(Collections.emptyList());

        var subject = subjectService.findAll();

        Assertions.assertThat(subject).isEmpty();
    }

    @Test
    void testFindAll() {
        Mockito.when(subjectRepo.findAll()).thenReturn(List.of(new Subject(UUID_ID, "Computer")));

        var subject = subjectService.findAll();

        Assertions.assertThat(subject).isNotEmpty();
        Assertions.assertThat(subject.size()).isEqualTo(1);
    }

    @Test
    void testDeleteSubject() {
        Subject subject = new Subject(UUID_ID, "Computer");
        subjectService.deleteById(subject.getId());
    }

    @Test
    void testSaveSubjectSuccess() {
        Subject subject = new Subject(UUID_ID, "Computer");

        Mockito.when(subjectRepo.save(subject)).thenReturn(subject);

        var sub = subjectService.save(subject);

        Assertions.assertThat(sub).isNotNull();
        Assertions.assertThat(sub.getName()).isEqualTo("Computer");

        Mockito.verify(subjectRepo, times(1)).save(subject);

    }

    @Test
    void testSaveSubjectFail() {
        Subject subject = new Subject(UUID_ID, "Computer");

        Mockito.when(subjectRepo.save(subject)).thenReturn(null);

        var cat = subjectService.save(subject);

        Assertions.assertThat(cat).isNull();

        Mockito.verify(subjectRepo, times(1)).save(subject);

    }

    @Test
    void testSaveSubjectError() {
        Subject subject = new Subject(UUID_ID, "Computer");

        subjectService.save(subject);

        Assertions.assertThatException();

        Mockito.verify(subjectRepo, times(1)).save(subject);

    }
}
