package com.example.sportal.controller;

import com.example.sportal.dto.article.ArticleDTO;

import com.example.sportal.model.exception.MethodNotAllowedException;

import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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
    public List<ArticleDTO> searchByTitle(@RequestParam("text") String text) {
        return articleService.searchByTitle(text);
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
    public ArticleDTO createArticle(@RequestParam(value = "title") String title,
                                    @RequestParam(value = "text") String text,
                                    @RequestParam(value = "categoryId") long categoryId,
                                    @RequestParam(value = "images") MultipartFile[] images,
                                    HttpServletRequest request) {
        long loggedAdminId = getLoggedAdminId(request);
        if (loggedAdminId != INVALID_USER_ID) {
            return articleService.createArticle(title, text, categoryId, images, loggedAdminId);
        }
        throw new MethodNotAllowedException("You don`t have permission to do this action!");
    }

    @PutMapping("/articles/{articleId}")
    public ArticleDTO edit(@PathVariable long articleId,
                           @RequestParam(value = "title") String title,
                           @RequestParam(value = "text") String text,
                           @RequestParam(value = "categoryId") long categoryId,
                           @RequestParam(value = "images") MultipartFile[] images,
                           HttpServletRequest request) {
        long loggedAdminId = getLoggedAdminId(request);
        if (loggedAdminId != INVALID_USER_ID) {
            return articleService.editArticle(articleId,title,text,categoryId,images);
        }
        throw new MethodNotAllowedException("You don`t have permission to do this action!");
    }

    @GetMapping("/categories/{categoryId}/articles")
    public List<ArticleDTO> getAllArticlesByCategoryId(@PathVariable long categoryId) {
        return articleService.getAllByCategoryId(categoryId);
    }

    @Scheduled(cron = "0 0 0 * * *")
    public void prepareDB() {
        articleService.clearDailyViews();
        resetPasswordLinkService.clearExpiredLinks();
    }
}
