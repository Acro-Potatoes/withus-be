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
@RequestMapping("/file")
public class FileController {

    private final S3Util s3Util;

    @Operation(summary = "프로필 이미지 업로드 API")
    @PostMapping("/profile")
    public ResponseEntity<Body> uploadImage(@RequestParam("image") MultipartFile multipartFile) throws IOException {
        return new ResponseSuccess().success(s3Util.uploadProfileImage(multipartFile));
    }

    @Operation(summary = "단일 파일 업로드 API")
    @PostMapping
    public ResponseEntity<Body> uploadFile(@RequestParam("file") MultipartFile multipartFile) throws IOException {
        return new ResponseSuccess().success(s3Util.uploadFile(multipartFile));
    }

    @Operation(summary = "다중 파일 업로드 API")
    @PostMapping("/multi")
    public ResponseEntity<Body> uploadFiles(@RequestParam("file") List<MultipartFile> multipartFile) throws IOException {
        s3Util.uploadFiles(multipartFile);
        return new ResponseSuccess().success();
    }

    @Operation(summary = "단일 파일 URL 조회 API")
    @GetMapping("/info")
    public ResponseEntity<Body> getFile(@RequestParam("filename") String fileName) {
        return new ResponseSuccess().success(s3Util.getFileUrl(fileName));
    }

    @Operation(summary = "단일 파일 삭제 API")
    @DeleteMapping
    public ResponseEntity<Body> deleteFile(@RequestParam("filename") String fileName) {
        s3Util.deleteFile(fileName);
        return new ResponseSuccess().success();
    }

}
