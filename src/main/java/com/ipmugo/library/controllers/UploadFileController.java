package com.ipmugo.library.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import com.ipmugo.library.data.File;
import com.ipmugo.library.dto.ResponseData;
import com.ipmugo.library.service.FileService;

@RestController
@RequestMapping("/api/upload")
public class UploadFileController {

    @Autowired
    private FileService fileService;

    @PostMapping("/image")
    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<ResponseData<String>> uploadImage(@RequestParam("image") MultipartFile file) {
        ResponseData<String> responseData = new ResponseData<>();
        try {
            File fileUpload = new File();

            UriComponents uriComponents = UriComponentsBuilder.newInstance().scheme("http").host("localhost:8080")
                    .path("/api/upload/image/" + file.getOriginalFilename()).build();

            fileUpload.setName(file.getOriginalFilename());
            fileUpload.setUrl(uriComponents.getPath());
            fileUpload.setFileByte(file.getBytes());

            byte[] checkFile = fileService.findFileName(file.getOriginalFilename());

            if (checkFile == null) {
                fileService.save(fileUpload);

                responseData.setStatus(true);
                responseData.setPayload(uriComponents.getPath());
                responseData.getMessages().add("Successfully uploaded filename " + file.getOriginalFilename());

                return ResponseEntity.ok(responseData);
            }

            responseData.setStatus(false);
            responseData.setPayload(null);
            responseData.getMessages().add("Uploaded file with " + file.getOriginalFilename() + "is ready");

            return ResponseEntity.badRequest().body(responseData);
        } catch (Exception e) {
            responseData.setStatus(false);
            responseData.getMessages().add(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseData);
        }
    }

    @GetMapping("/image/{name}")
    public ResponseEntity<?> getFileNameImage(@PathVariable("name") String fileName) {
        ResponseData<?> responseData = new ResponseData<>();
        try {
            byte[] image = fileService.findFileName(fileName);

            return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(image);
        } catch (Exception e) {
            responseData.setStatus(false);
            responseData.getMessages().add(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseData);
        }
    }

    @PostMapping("/document")
    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<ResponseData<String>> uploadDoc(@RequestParam("document") MultipartFile file) {
        ResponseData<String> responseData = new ResponseData<>();
        try {
            File fileUpload = new File();

            UriComponents uriComponents = UriComponentsBuilder.newInstance().scheme("http").host("localhost:8080")
                    .path("/api/upload/document/" + file.getOriginalFilename()).build();

            fileUpload.setName(file.getOriginalFilename());
            fileUpload.setUrl(uriComponents.getPath());
            fileUpload.setFileByte(file.getBytes());

            byte[] checkFile = fileService.findFileName(file.getOriginalFilename());

            if (checkFile == null) {
                fileService.save(fileUpload);

                responseData.setStatus(true);
                responseData.setPayload(uriComponents.getPath());
                responseData.getMessages().add("Successfully uploaded filename " + file.getOriginalFilename());
                return ResponseEntity.ok(responseData);
            }

            responseData.setStatus(false);
            responseData.setPayload(null);
            responseData.getMessages().add("Uploaded file with " + file.getOriginalFilename() + "is ready");

            return ResponseEntity.badRequest().body(responseData);
        } catch (Exception e) {
            responseData.setStatus(false);
            responseData.getMessages().add(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseData);
        }
    }

    @GetMapping("/document/{name}")
    public ResponseEntity<?> getFileName(@PathVariable("name") String fileName) {
        ResponseData<?> responseData = new ResponseData<>();
        try {
            byte[] document = fileService.findFileName(fileName);

            return ResponseEntity.ok().contentType(MediaType.APPLICATION_PDF).body(document);
        } catch (Exception e) {
            responseData.setStatus(false);
            responseData.getMessages().add(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseData);
        }
    }
}
