package com.kowal.backend.admin.controller;

import com.kowal.backend.acrticle.dto.request.ArticleRequest;
import com.kowal.backend.acrticle.dto.response.ArticleDetailsResponse;
import com.kowal.backend.acrticle.dto.response.ArticlesResponse;
import com.kowal.backend.acrticle.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminController {

    private final ArticleService articleService;


    @Autowired
    public AdminController(ArticleService articleService) {
        this.articleService = articleService;
    }

    @GetMapping("/articles")
    public ResponseEntity<List<ArticleDetailsResponse>> getAllArticles(@AuthenticationPrincipal  UserDetails userDetails){
        String userEmail = userDetails.getUsername();
        List<ArticleDetailsResponse> articles = articleService.getAllArticlesDetails(userEmail);
        return ResponseEntity.ok(articles);
    }

    @PostMapping("/articles")
    public ResponseEntity<ArticleDetailsResponse> addArticle(@ModelAttribute ArticleRequest articleRequest, @AuthenticationPrincipal UserDetails userDetails){
        String userEmail = userDetails.getUsername();
        ArticleDetailsResponse article = articleService.addArticle(userEmail, articleRequest);
        return ResponseEntity.ok(article);
    }

    @PutMapping("/articles/{articleId}")
    public ResponseEntity<ArticleDetailsResponse> updateArticle(@PathVariable Long articleId, @ModelAttribute ArticleRequest articleRequest,
                                                          @AuthenticationPrincipal UserDetails userDetails){
        String userEmail = userDetails.getUsername();
        ArticleDetailsResponse article = articleService.updateArticle(userEmail, articleId, articleRequest);
        return ResponseEntity.ok(article);
    }

    @DeleteMapping("/articles/{articleId}")
    public ResponseEntity<ArticleDetailsResponse> deleteArticle(@PathVariable Long articleId, @AuthenticationPrincipal UserDetails userDetails){
        String userEmail = userDetails.getUsername();
        ArticleDetailsResponse article = articleService.deleteArticle(userEmail, articleId);
        return ResponseEntity.ok(article);
    }

}
