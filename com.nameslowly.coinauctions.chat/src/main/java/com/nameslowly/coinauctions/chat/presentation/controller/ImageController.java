package com.nameslowly.coinauctions.chat.presentation.controller;

import com.nameslowly.coinauctions.chat.application.service.ImageService;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@AllArgsConstructor
@RestController
@RequestMapping("/api/chat/images") //여기 수정 필요
public class ImageController {

    private final ImageService imageService;

    @PostMapping
    @SneakyThrows
    public ResponseEntity<String> uploadImage(@RequestParam("file") MultipartFile file) {
        String uploadFileName = imageService.uploadImage(file);
        return ResponseEntity.ok(uploadFileName);
    }

}