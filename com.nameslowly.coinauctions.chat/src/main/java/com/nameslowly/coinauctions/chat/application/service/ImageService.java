package com.nameslowly.coinauctions.chat.application.service;


import com.nameslowly.coinauctions.chat.domain.model.ImageExtension;
import com.nameslowly.coinauctions.common.exception.GlobalException;
import com.nameslowly.coinauctions.common.response.ResultCase;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.awt.*;
import java.util.UUID;

@Service
public class ImageService {

    final long MAX_FILE_SIZE = 10 * 1024 * 1024; // 10MB
    final String S3path = "https://basicchat2.s3.ap-northeast-2.amazonaws.com/images/";

    private final S3Client s3Client;
    private final String bucketName;
    private final String s3Path;

    public ImageService(
            S3Client s3Client,
            @Value("${s3.bucket-name}") String bucketName,
            @Value("${s3.path}") String s3Path
    ) {
        this.s3Client = s3Client;
        this.bucketName = bucketName;
        this.s3Path = s3Path;
    }

    @Transactional
    @SneakyThrows
    //@Async
    public String uploadImage(MultipartFile file) {
        String originalFileName = file.getOriginalFilename();
        if(originalFileName == null) throw new Exception("잘못된 파일입니다.");

        if(file.getSize() > MAX_FILE_SIZE) {
            throw new GlobalException(ResultCase.CANT_UPLOAD_OVER_10MB);
        }

        // test_image.jpg -> [0]:test_image, [1]:jpg
        String[] fileInfos = splitFileName(originalFileName);
        ImageExtension ext = ImageExtension.findByKey(fileInfos[1]).orElseThrow(
                () -> new GlobalException(ResultCase.EXTENSION_NOT_PROVIDED));
        String uploadFileName = UUID.randomUUID() + "." + ext.getKey();
        uploadImageToS3(uploadFileName, file);
        return S3path + uploadFileName;
    }


    @SneakyThrows
    public void uploadImageToS3(String uploadFileName, MultipartFile file) {
        String uploadUrl = s3Path + "/" + uploadFileName;

        PutObjectRequest request = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(uploadUrl)
                .build();

        RequestBody content = RequestBody.fromInputStream(file.getInputStream(), file.getSize());
        s3Client.putObject(request, content);
    }


    public String[] splitFileName(String fileName) {
        int lastDotIndex = fileName.lastIndexOf(".");
        if (lastDotIndex == -1) {
            return new String[]{fileName, ""};
        }

        String name = fileName.substring(0, lastDotIndex);
        String ext = fileName.substring(lastDotIndex + 1);
        return new String[]{name, ext};
    }
}