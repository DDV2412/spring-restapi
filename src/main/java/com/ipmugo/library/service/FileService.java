package com.ipmugo.library.service;

import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ipmugo.library.data.File;
import com.ipmugo.library.repository.FileRepo;

import jakarta.transaction.TransactionScoped;

@Service
@TransactionScoped
public class FileService {

    @Autowired
    private FileRepo fileRepo;

    public File save(File file) {
        return fileRepo.save(file);
    }

    public byte[] findFileName(String name) {
        Optional<File> file = fileRepo.findByName(name);

        if (!file.isPresent()) {
            return null;
        }

        return file.get().getImageData();
    }

    public void deleteById(UUID id) {
        fileRepo.deleteById(id);
    }
}
