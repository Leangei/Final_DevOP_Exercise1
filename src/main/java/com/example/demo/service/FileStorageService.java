package com.example.demo.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class FileStorageService {

    private static final long MAX_SIZE = 5 * 1024 * 1024; // 5MB

    private final Path root = Paths.get("uploads/photos");

    public String store(MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            throw new RuntimeException("Empty file");
        }

        if (file.getSize() > MAX_SIZE) {
            throw new RuntimeException("File size exceeds maximum limit of 5MB");
        }

        String type = file.getContentType();

        if (!"image/jpeg".equals(type) && !"image/png".equals(type)) {
            throw new RuntimeException("Only JPEG and PNG images are allowed");
        }

        Files.createDirectories(root);

        String filename = UUID.randomUUID() + "_" + file.getOriginalFilename();

        Files.copy(file.getInputStream(), root.resolve(filename));

        return filename;
    }

    public Path load(String filename) {
        return root.resolve(filename);
    }
}