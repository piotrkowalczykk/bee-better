package com.kowal.backend.acrticle.dto.request;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class ArticleRequest {
    private String title;
    private String content;
    private String description;
    private MultipartFile image;
}
