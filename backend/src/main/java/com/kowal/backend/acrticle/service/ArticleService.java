package com.kowal.backend.acrticle.service;

import com.kowal.backend.acrticle.dto.request.ArticleRequest;
import com.kowal.backend.acrticle.dto.response.ArticleDetailsResponse;
import com.kowal.backend.acrticle.dto.response.ArticlesResponse;
import com.kowal.backend.acrticle.model.Article;

import java.util.List;

public interface ArticleService {
    List<ArticlesResponse> getAllArticles(String userEmail);

    ArticleDetailsResponse getArticleById(String userEmail, Long articleId);

    ArticleDetailsResponse addArticle(String userEmail, ArticleRequest articleRequest);

    ArticleDetailsResponse updateArticle(String userEmail, Long articleId, ArticleRequest articleRequest);

    ArticleDetailsResponse deleteArticle(String userEmail, Long articleId);

    List<ArticleDetailsResponse> getAllArticlesDetails(String userEmail);
}
