package com.example.sportal.repository;

import com.example.sportal.model.entity.Article;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ArticleRepository extends JpaRepository<Article, Long> {

    @Query(value = "SELECT * FROM articles ORDER BY daily_views DESC LIMIT 5",
            nativeQuery = true)
        //TODO: TEST
    Optional<List<Article>> findAllOrderByDailyViewsDesc();

    Optional<List<Article>> findAllByCategoryName(String categoryName);
}
