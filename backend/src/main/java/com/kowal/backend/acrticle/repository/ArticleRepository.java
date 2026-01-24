package com.kowal.backend.acrticle.repository;

import com.kowal.backend.acrticle.model.Article;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArticleRepository extends JpaRepository<Article, Long> {

}
