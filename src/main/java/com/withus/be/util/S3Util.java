package com.withus.be.util;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class S3Util {

    private final AmazonS3Client amazonS3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    // TODO image url DB 저장
    public String uploadFile(MultipartFile multipartFile) throws IOException {
        String s3FileName = getS3FileName(multipartFile);
        putObjectToS3(multipartFile, s3FileName);
        return s3FileName;
    }

    public void uploadFiles(List<MultipartFile> multipartFiles) throws IOException {
        for (MultipartFile multipartFile : multipartFiles) {
            putObjectToS3(multipartFile, getS3FileName(multipartFile));
        }
    }

    public String getFileUrl(String fileName) {
        return amazonS3Client.getUrl(bucket, fileName).toString();
    }

    private static String getS3FileName(MultipartFile multipartFile) {
        return UUID.randomUUID() + "-" + multipartFile.getOriginalFilename();
    }

    private void putObjectToS3(MultipartFile multipartFile, String s3FileName) throws IOException {
        InputStream inputStream = multipartFile.getInputStream();
        ObjectMetadata objMeta = new ObjectMetadata();
        objMeta.setContentType(multipartFile.getContentType());

        amazonS3Client.putObject(bucket, s3FileName, inputStream, objMeta);
    }

    public void deleteFile(String fileName) {
        if (!amazonS3Client.doesObjectExist(bucket, fileName)) return;
        amazonS3Client.deleteObject(bucket, fileName);
    }
}
