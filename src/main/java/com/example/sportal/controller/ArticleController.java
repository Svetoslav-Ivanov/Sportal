package com.example.sportal.controller;

import com.example.sportal.dto.article.ArticleDTO;
import com.example.sportal.dto.article.EditArticleDTO;

import com.example.sportal.dto.article.NewArticleDTO;
import com.example.sportal.model.exception.MethodNotAllowedException;
import com.example.sportal.model.exception.NotFoundException;
import com.example.sportal.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

@RestController()
@RequestMapping("/articles")
public class ArticleController extends AbstractController {

    @Autowired
    private ArticleService articleService;

    @GetMapping("/{aid}")
    public ArticleDTO getArticleById(@PathVariable long aid) {
        return articleService.getById(aid);
    }

    @GetMapping("/{categoryName}")
    public List<ArticleDTO> getAllArticlesByCategoryName(@PathVariable String categoryName) {
        return articleService.getAllByCategoryName(categoryName);
    }

    @GetMapping("/top_five_today")
    public List<ArticleDTO> getTopFiveArticlesByDailyViews() {
        return articleService.getTopFiveByDailyViews();
    }

    @DeleteMapping("/{aid}")
    public String deleteArticle(@PathVariable long aid, HttpServletRequest request) {
        if (getLoggedAdminId(request) != INVALID_USER_ID) {
            if (articleService.deleteById(aid)){
                return "Article has been deleted successfully";
            } else {
                throw new NotFoundException("Article not found!");
            }
        }
        throw new MethodNotAllowedException("You don`t have permission to do this action!");
    }

    @PostMapping()
    @ResponseStatus(code = HttpStatus.CREATED)
    public ArticleDTO createArticle(@RequestBody NewArticleDTO dto, HttpServletRequest request) {
        long loggedAdminId = getLoggedAdminId(request);
        if (loggedAdminId!=INVALID_USER_ID){
            return articleService.createArticle(dto, loggedAdminId);
        }
        throw new MethodNotAllowedException("You don`t have permission to do this action!");
    }

    @PutMapping()
    @ResponseStatus(code = HttpStatus.OK)
    public ArticleDTO edit(@RequestBody EditArticleDTO articleEditDTO, HttpServletRequest request) {
        long loggedAdminId = getLoggedAdminId(request);
        if (loggedAdminId!=INVALID_USER_ID){
            return articleService.editArticle(articleEditDTO);
        }
        throw new MethodNotAllowedException("You don`t have permission to do this action!");
    }

}
