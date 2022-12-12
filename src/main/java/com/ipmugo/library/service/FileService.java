package com.ipmugo.library.service;

import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

import jakarta.annotation.PostConstruct;

@Service
public class FileService {

    @Value("${upload.path-document}")
    private String uploadDocument;

    @Value("${upload.path-image}")
    private String uploadImage;

    @Value("${upload.path-figure}")
    private String uploadFigure;

    @PostConstruct
    public void init() {
        try {
            Files.createDirectories(Paths.get(uploadDocument));

            Files.createDirectories(Paths.get(uploadImage));

            Files.createDirectories(Paths.get(uploadFigure));

        } catch (Exception e) {
            throw new RuntimeException("Could not create upload folder!");
        }
    }

    public void saveDocument(MultipartFile file) {
        try {

            Path root = Paths.get(uploadDocument);
            if (!Files.exists(root)) {
                init();
            }

            Files.copy(file.getInputStream(), root.resolve(file.getOriginalFilename()));
        } catch (Exception e) {
            throw new RuntimeException("Could not store the file. Error: " + e.getMessage());
        }
    }

    public void saveImage(MultipartFile file) {
        try {

            Path root = Paths.get(uploadImage);
            if (!Files.exists(root)) {
                init();
            }

            Files.copy(file.getInputStream(), root.resolve(file.getOriginalFilename()));
        } catch (Exception e) {
            throw new RuntimeException("Could not store the file. Error: " + e.getMessage());
        }
    }

    public void saveFigure(MultipartFile file) {
        try {

            Path root = Paths.get(uploadFigure);
            if (!Files.exists(root)) {
                init();
            }

            Files.copy(file.getInputStream(), root.resolve(file.getOriginalFilename()));
        } catch (Exception e) {
            throw new RuntimeException("Could not store the file. Error: " + e.getMessage());
        }
    }

    public Resource loadDocument(String filename) {
        try {
            Path file = Paths.get(
                    uploadDocument)
                    .resolve(filename);
            Resource resource = new UrlResource(file.toUri());

            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                return null;
            }
        } catch (MalformedURLException e) {
            return null;
        }
    }

    public Resource loadImage(String filename) {
        try {
            Path file = Paths.get(
                    uploadImage)
                    .resolve(filename);
            Resource resource = new UrlResource(file.toUri());

            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                return null;
            }
        } catch (MalformedURLException e) {
            return null;
        }
    }

    public Resource loadFigure(String filename) {
        try {
            Path file = Paths.get(
                    uploadFigure)
                    .resolve(filename);
            Resource resource = new UrlResource(file.toUri());

            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                return null;
            }
        } catch (MalformedURLException e) {
            return null;
        }
    }

    public void deleteDocument(String filename) {
        FileSystemUtils.deleteRecursively(Paths.get(
                uploadDocument).resolve(filename)
                .toFile());
    }

    public void deleteImage(String filename) {
        FileSystemUtils.deleteRecursively(Paths.get(
                uploadImage).resolve(filename)
                .toFile());
    }

    public void deleteFigure(String filename) {
        FileSystemUtils.deleteRecursively(Paths.get(
                uploadFigure).resolve(filename)
                .toFile());
    }

}
