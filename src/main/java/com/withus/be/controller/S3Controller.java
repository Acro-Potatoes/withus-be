package com.withus.be.controller;

import com.withus.be.common.response.Response.Body;
import com.withus.be.common.response.ResponseSuccess;
import com.withus.be.util.S3Util;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class S3Controller {

    private final S3Util s3Util;

    @PostMapping("/image")
    public ResponseEntity<Body> uploadFile(@RequestParam("file") MultipartFile multipartFile) throws IOException {
        return new ResponseSuccess().success(s3Util.uploadFile(multipartFile));
    }

    @PostMapping("/images")
    public ResponseEntity<Body> uploadFiles(@RequestParam("file") List<MultipartFile> multipartFile) throws IOException {
        s3Util.uploadFiles(multipartFile);
        return new ResponseSuccess().success();
    }

    @GetMapping("/images/info")
    public ResponseEntity<Body> getFile(@RequestParam("filename") String fileName) {
        return new ResponseSuccess().success(s3Util.getFileUrl(fileName));
    }

    @DeleteMapping("/images")
    public ResponseEntity<Body> deleteFile(@RequestParam("filename") String fileName) {
        s3Util.deleteFile(fileName);
        return new ResponseSuccess().success();
    }

}
