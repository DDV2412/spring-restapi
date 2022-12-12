package com.ipmugo.library.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import com.ipmugo.library.dto.FileData;
import com.ipmugo.library.dto.ResponseData;
import com.ipmugo.library.service.FileService;

@RestController
@RequestMapping("/api/upload")
public class UploadFileController {

    @Autowired
    private FileService fileService;

    @PostMapping("/document")
    public ResponseEntity<ResponseData<FileData>> uploadDoc(@RequestParam("upload") MultipartFile file) {
        ResponseData<FileData> responseData = new ResponseData<>();
        try {

            if (file.isEmpty()) {
                responseData.setStatus(false);
                responseData.setPayload(null);
                responseData.getMessages().add("Please select a file to upload");

                return ResponseEntity.badRequest().body(responseData);
            }

            fileService.saveDocument(file);

            FileData fileData = new FileData();

            fileData.setFilename(file.getOriginalFilename());
            fileData.setSize(file.getSize());
            fileData.setUrl(MvcUriComponentsBuilder
                    .fromMethodName(UploadFileController.class, "getDocument", file.getOriginalFilename()).build()
                    .toString());

            responseData.setStatus(true);
            responseData.setPayload(fileData);
            responseData.getMessages().add("Uploaded file with " + file.getOriginalFilename() + "is successfully");

            return ResponseEntity.ok(responseData);
        } catch (Exception e) {
            responseData.setStatus(false);
            responseData.getMessages().add(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseData);
        }
    }

    @PostMapping("/image")
    public ResponseEntity<ResponseData<FileData>> uploadImage(@RequestParam("upload") MultipartFile file) {
        ResponseData<FileData> responseData = new ResponseData<>();
        try {

            if (file.isEmpty()) {
                responseData.setStatus(false);
                responseData.setPayload(null);
                responseData.getMessages().add("Please select a file to upload");

                return ResponseEntity.badRequest().body(responseData);
            }

            fileService.saveImage(file);

            FileData fileData = new FileData();

            fileData.setFilename(file.getOriginalFilename());
            fileData.setSize(file.getSize());
            fileData.setUrl(MvcUriComponentsBuilder
                    .fromMethodName(UploadFileController.class, "getImage", file.getOriginalFilename()).build()
                    .toString());

            responseData.setStatus(true);
            responseData.setPayload(fileData);
            responseData.getMessages().add("Uploaded file with " + file.getOriginalFilename() + "is successfully");

            return ResponseEntity.ok(responseData);
        } catch (Exception e) {
            responseData.setStatus(false);
            responseData.getMessages().add(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseData);
        }
    }

    @GetMapping("/document/{filename:.+}")
    public ResponseEntity<Resource> getDocument(@PathVariable String filename) {

        Resource file = fileService.loadDocument(filename);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"")
                .body(file);
    }

    @GetMapping("/image/{filename:.+}")
    public ResponseEntity<Resource> getImage(@PathVariable String filename) {
        Resource file = fileService.loadImage(filename);

        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_JPEG)
                .contentType(MediaType.IMAGE_PNG)
                .contentType(MediaType.valueOf("image/jpg"))
                .body(file);
    }

    @GetMapping("/figure/{filename:.+}")
    public ResponseEntity<Resource> getFigure(@PathVariable String filename) {
        Resource file = fileService.loadFigure(filename);

        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_JPEG)
                .contentType(MediaType.IMAGE_PNG)
                .contentType(MediaType.valueOf("image/jpg"))
                .body(file);
    }

    @DeleteMapping("/delete/figure/{filename:.+}")
    public ResponseEntity<ResponseData<String>> delFigure(@PathVariable String filename) {
        ResponseData<String> responseData = new ResponseData<>();

        fileService.deleteFigure(filename);

        responseData.setStatus(true);
        responseData.setPayload(null);
        responseData.getMessages().add("Deleted file with " + filename + "is successfully");

        return ResponseEntity.ok(responseData);
    }

    @DeleteMapping("/delete/image/{filename:.+}")
    public ResponseEntity<ResponseData<String>> delImage(@PathVariable String filename) {
        ResponseData<String> responseData = new ResponseData<>();

        fileService.deleteImage(filename);

        responseData.setStatus(true);
        responseData.setPayload(null);
        responseData.getMessages().add("Deleted file with " + filename + "is successfully");

        return ResponseEntity.ok(responseData);
    }

    @DeleteMapping("/delete/document/{filename:.+}")
    public ResponseEntity<ResponseData<String>> delDocument(@PathVariable String filename) {
        ResponseData<String> responseData = new ResponseData<>();

        fileService.deleteDocument(filename);

        responseData.setStatus(true);
        responseData.setPayload(null);
        responseData.getMessages().add("Deleted file with " + filename + "is successfully");

        return ResponseEntity.ok(responseData);
    }

}
