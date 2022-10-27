package com.example.sportal.controller;

import com.example.sportal.dto.article.ArticleDTO;
import com.example.sportal.dto.article.EditArticleDTO;

import com.example.sportal.dto.article.NewArticleDTO;
import com.example.sportal.dto.article.SearchArticleDTO;
import com.example.sportal.model.exception.MethodNotAllowedException;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController()
public class ArticleController extends AbstractController {

    @GetMapping("/articles/{articleId}")
    public ArticleDTO getArticleById(@PathVariable long articleId) {
        return articleService.getById(articleId);
    }

    @GetMapping("/articles/top_five_today")
    public List<ArticleDTO> getTopFiveArticlesByDailyViews() {
        return articleService.getTopFiveByDailyViews();
    }

    @GetMapping("/articles")
    public List<ArticleDTO> searchByTitle(@RequestBody SearchArticleDTO dto) {
        return articleService.searchByTitle(dto);
    }

    @DeleteMapping("/articles/{articleId}")
    public ArticleDTO deleteArticle(@PathVariable long articleId, HttpServletRequest request) {
        if (getLoggedAdminId(request) != INVALID_USER_ID) {
                return articleService.deleteById(articleId);
        }
        throw new MethodNotAllowedException("You don`t have permission to do this action!");
    }

    @PostMapping("/articles")
    @ResponseStatus(code = HttpStatus.CREATED)
    public ArticleDTO createArticle(@RequestBody NewArticleDTO dto, HttpServletRequest request) {
        long loggedAdminId = getLoggedAdminId(request);
        if (loggedAdminId!=INVALID_USER_ID){
            return articleService.createArticle(dto, loggedAdminId);
        }
        throw new MethodNotAllowedException("You don`t have permission to do this action!");
    }

    @PutMapping("/articles")
    public ArticleDTO edit(@RequestBody EditArticleDTO articleEditDTO, HttpServletRequest request) {
        long loggedAdminId = getLoggedAdminId(request);
        if (loggedAdminId!=INVALID_USER_ID){
            return articleService.editArticle(articleEditDTO);
        }
        throw new MethodNotAllowedException("You don`t have permission to do this action!");
    }

    @GetMapping("/categories/{categoryId}/articles")
    public List<ArticleDTO> getAllArticlesByCategoryName(@PathVariable long categoryId) {
        return articleService.getAllByCategoryId(categoryId);
    }


    @Scheduled(cron = "0 0 0 * * *")
    public void clearDailyViews() {
        articleService.clearDailyViews();
    }
}
