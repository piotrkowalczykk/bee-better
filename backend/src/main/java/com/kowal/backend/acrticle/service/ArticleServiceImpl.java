package com.kowal.backend.acrticle.service;

import com.kowal.backend.acrticle.dto.request.ArticleRequest;
import com.kowal.backend.acrticle.dto.response.ArticleDetailsResponse;
import com.kowal.backend.acrticle.dto.response.ArticlesResponse;
import com.kowal.backend.acrticle.mapper.ArticleMapper;
import com.kowal.backend.acrticle.model.Article;
import com.kowal.backend.acrticle.repository.ArticleRepository;
import com.kowal.backend.exception.security.ArticleNotFoundException;
import com.kowal.backend.security.model.AuthUser;
import com.kowal.backend.security.repository.AuthUserRepository;
import com.kowal.backend.util.FileStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ArticleServiceImpl implements ArticleService {

    private final AuthUserRepository authUserRepository;
    private final ArticleRepository articleRepository;
    private final ArticleMapper articleMapper;
    private final FileStorageService fileStorageService;

    @Autowired
    public ArticleServiceImpl(AuthUserRepository authUserRepository, ArticleRepository articleRepository, ArticleMapper articleMapper
    , FileStorageService fileStorageService) {
        this.authUserRepository = authUserRepository;
        this.articleRepository = articleRepository;
        this.articleMapper = articleMapper;
        this.fileStorageService = fileStorageService;
    }

    @Override
    public List<ArticlesResponse> getAllArticles(String userEmail) {
        AuthUser authUser = findAuthUserByEmail(userEmail);
        List<Article> articles = articleRepository.findAll();
        return articleMapper.toArticlesResponseList(articles);
    }

    @Override
    public ArticleDetailsResponse getArticleById(String userEmail, Long articleId) {
        AuthUser authUser = findAuthUserByEmail(userEmail);
        Article article = articleRepository.findById(articleId)
                .orElseThrow(() -> new ArticleNotFoundException("Article not found with id: " + articleId));

        return articleMapper.toArticleDetails(article);
    }

    @Override
    public ArticleDetailsResponse addArticle(String userEmail, ArticleRequest articleRequest) {
        AuthUser authUser = findAuthUserByEmail(userEmail);
        Article article = new Article();
        article.setTitle(articleRequest.getTitle());
        article.setDescription(articleRequest.getDescription());
        article.setImageUrl(fileStorageService.storeFile(articleRequest.getImage(), "articles"));
        article.setContent(articleRequest.getContent());
        article.setCreatedAt(LocalDateTime.now());
        article.setModifiedAt(null);
        articleRepository.save(article);
        return articleMapper.toArticleDetails(article);
    }

    @Override
    public ArticleDetailsResponse updateArticle(String userEmail, Long articleId, ArticleRequest articleRequest) {
        AuthUser authUser = findAuthUserByEmail(userEmail);
        Article article = articleRepository.findById(articleId)
                .orElseThrow(() -> new ArticleNotFoundException("Article not found with id: " + articleId));
        if(articleRequest.getTitle() != null) article.setTitle(articleRequest.getTitle());
        if(articleRequest.getDescription() != null) article.setDescription(articleRequest.getDescription());
        if(articleRequest.getImage() != null) {
            fileStorageService.deleteFile(article.getImageUrl());
            String imageUrl = fileStorageService.storeFile(articleRequest.getImage(), "articles");
            article.setImageUrl(imageUrl);
        }
        if(articleRequest.getContent() != null) article.setContent(articleRequest.getContent());
        article.setModifiedAt(LocalDateTime.now());
        articleRepository.save(article);
        return articleMapper.toArticleDetails(article);
    }

    @Override
    public ArticleDetailsResponse deleteArticle(String userEmail, Long articleId) {
        AuthUser authUser = findAuthUserByEmail(userEmail);
        Article article = articleRepository.findById(articleId)
                .orElseThrow(() -> new ArticleNotFoundException("Article not found with id: " + articleId));
        fileStorageService.deleteFile(article.getImageUrl());
        articleRepository.delete(article);
        return articleMapper.toArticleDetails(article);
    }

    @Override
    public List<ArticleDetailsResponse> getAllArticlesDetails(String userEmail) {
        AuthUser authUser = findAuthUserByEmail(userEmail);
        List<Article> articles = articleRepository.findAll();
        return articleMapper.toArticlesDetailsResponseList(articles);
    }

    private AuthUser findAuthUserByEmail(String userEmail) {
        return authUserRepository.findByEmail(userEmail)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + userEmail));
    }
}
