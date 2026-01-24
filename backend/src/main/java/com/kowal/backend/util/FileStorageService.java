package com.kowal.backend.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

@Service
public class FileStorageService {

    private static final Set<String> ALLOWED_IMAGE_EXTENSIONS = Set.of("jpg", "jpeg", "png", "webp");

    @Value("${exercise.images.base-url}")
    private String baseUrl;

    @Value("${exercise.images.dir}")
    private String exercisesDir;

    @Value("${article.images.dir}")
    private String articlesDir;

    public String storeFile(MultipartFile file, String folder) {
        validateFile(file);

        String extension = getFileExtension(file.getOriginalFilename());
        String uniqueName = UUID.randomUUID() + "." + extension;

        Path targetDir;
        switch (folder.toLowerCase()) {
            case "articles":
                targetDir = Paths.get(articlesDir);
                break;
            case "exercises":
            default:
                targetDir = Paths.get(exercisesDir);
                break;
        }

        try {
            if (!Files.exists(targetDir)) {
                Files.createDirectories(targetDir);
            }

            Path targetPath = targetDir.resolve(uniqueName).normalize();
            Files.copy(file.getInputStream(), targetPath, StandardCopyOption.REPLACE_EXISTING);

            return baseUrl + folder + "/" + uniqueName;

        } catch (IOException e) {
            throw new RuntimeException("Could not store file: " + file.getOriginalFilename(), e);
        }
    }

    private void validateFile(MultipartFile file) {
        if (file.isEmpty()) throw new IllegalArgumentException("Cannot store empty file");

        String originalFilename = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
        if (originalFilename.contains("..")) {
            throw new IllegalArgumentException("Filename contains invalid path sequence: " + originalFilename);
        }

        String extension = getFileExtension(originalFilename);
        if (!ALLOWED_IMAGE_EXTENSIONS.contains(extension.toLowerCase())) {
            throw new IllegalArgumentException("Invalid file type: ." + extension +
                    ". Allowed types: " + ALLOWED_IMAGE_EXTENSIONS);
        }
    }

    private String getFileExtension(String filename) {
        String ext = StringUtils.getFilenameExtension(filename);
        if (ext == null) throw new IllegalArgumentException("File must have an extension");
        return ext.toLowerCase();
    }

    public void deleteFile(String fileUrl) {
        if (fileUrl == null || fileUrl.isEmpty()) return;

        try {
            String filename = Paths.get(fileUrl).getFileName().toString();
            Path filePath = Paths.get(exercisesDir).resolve(filename).normalize();
            Files.deleteIfExists(filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
