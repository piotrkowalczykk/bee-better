package com.kowal.backend.acrticle.mapper;

import com.kowal.backend.acrticle.dto.response.ArticleDetailsResponse;
import com.kowal.backend.acrticle.dto.response.ArticlesResponse;
import com.kowal.backend.acrticle.model.Article;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ArticleMapper {
    public ArticlesResponse toArticlesResponse(Article article) {
        if (article == null) return null;

        return new ArticlesResponse(
                article.getId(),
                article.getTitle(),
                article.getDescription(),
                article.getImageUrl(),
                article.getCreatedAt(),
                article.getModifiedAt()
        );
    }

    public List<ArticlesResponse> toArticlesResponseList(List<Article> articles) {
        if (articles == null) return List.of();

        return articles.stream()
                .map(this::toArticlesResponse)
                .toList();
    }

    public ArticleDetailsResponse toArticleDetails(Article article) {
        if (article == null) return null;

        return new ArticleDetailsResponse(
                article.getId(),
                article.getTitle(),
                article.getDescription(),
                article.getContent(),
                article.getImageUrl(),
                article.getCreatedAt(),
                article.getModifiedAt()
        );
    }

    public List<ArticleDetailsResponse> toArticlesDetailsResponseList(List<Article> articles) {
        if (articles == null) return List.of();

        return articles.stream()
                .map(this::toArticleDetails)
                .toList();
    }
}

