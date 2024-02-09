package com.withus.be.controller;

import com.withus.be.common.response.Response.Body;
import com.withus.be.common.response.ResponseSuccess;
import com.withus.be.util.S3Util;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

/**
 * S3 관련 컨트롤러로 추후 수정 예정
 */
@RestController
@RequiredArgsConstructor
public class S3Controller {

    private final S3Util s3Util;

    @Operation(summary = "단일 파일 업로드 API")
    @PostMapping("/image")
    public ResponseEntity<Body> uploadFile(@RequestParam("file") MultipartFile multipartFile) throws IOException {
        return new ResponseSuccess().success(s3Util.uploadFile(multipartFile));
    }

    @Operation(summary = "다중 파일 업로드 API")
    @PostMapping("/images")
    public ResponseEntity<Body> uploadFiles(@RequestParam("file") List<MultipartFile> multipartFile) throws IOException {
        s3Util.uploadFiles(multipartFile);
        return new ResponseSuccess().success();
    }

    @Operation(summary = "단일 파일 이름 조회 API")
    @GetMapping("/image/info")
    public ResponseEntity<Body> getFile(@RequestParam("filename") String fileName) {
        return new ResponseSuccess().success(s3Util.getFileUrl(fileName));
    }

    @Operation(summary = "단일 파일 삭제 API")
    @DeleteMapping("/image")
    public ResponseEntity<Body> deleteFile(@RequestParam("filename") String fileName) {
        s3Util.deleteFile(fileName);
        return new ResponseSuccess().success();
    }

}
