package com.example.sportal.service;

import com.example.sportal.dto.article.ArticleDTO;
import com.example.sportal.dto.article.EditArticleDTO;
import com.example.sportal.dto.article.NewArticleDTO;
import com.example.sportal.model.entity.Article;
import com.example.sportal.model.exception.NotFoundException;
import com.example.sportal.repository.CategoryRepository;

import com.example.sportal.util.validators.ArticleValidator;
import org.modelmapper.ModelMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ArticleService extends AbstractService {
    //TODO: Refactor
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private ArticleValidator articleValidator;


    public ArticleDTO getById(long id) {
        Article article = getArticleById(id);
        article.setViews(article.getViews() + 1);
        articleRepository.save(article);
        return modelMapper.map(article, ArticleDTO.class);
    }

    public List<ArticleDTO> getAllByCategoryName(String categoryName) {
        List<Article> articles = articleRepository.findAllByCategoryName(categoryName)
                .orElseThrow(() -> new NotFoundException("Category not found!"));

        return articles
                .stream()
                .map(a -> modelMapper.map(a, ArticleDTO.class))
                .collect(Collectors.toList());
    }

    public List<ArticleDTO> getTopFiveByDailyViews() {
        List<Article> articles = articleRepository
                .findAllOrderByDailyViewsDesc()
                .orElseThrow(() -> new NotFoundException("Articles not found"));
        return articles
                .stream()
                .map(a -> modelMapper.map(a, ArticleDTO.class))
                .collect(Collectors.toList());
    }

    public boolean deleteById(long id) {
        Article article = getArticleById(id);
        articleRepository.delete(article);
        return true;
    }

    public ArticleDTO createArticle(NewArticleDTO dto, long authorId) {
        Article article = modelMapper.map(dto, Article.class);
        if (articleValidator.isValidDTO(dto)) {
            article.setPostDate(Calendar.getInstance().getTime());
            article.setAuthor(getUserById(authorId));
            //TODO: images
            articleRepository.save(article);
            return modelMapper.map(article, ArticleDTO.class);
        }
        return null;
    }

    public ArticleDTO editArticle(EditArticleDTO dto) {
        Article article = getArticleById(dto.getArticleId());
        if (articleValidator.isValidDTO(dto)) {
            article.setTitle(dto.getTitle());
            article.setText(dto.getText());
            article.setCategory(categoryRepository.getByName(dto.getCategoryName()));
            //TODO: images
            articleRepository.save(article);
            return modelMapper.map(article, ArticleDTO.class);
        }
        return null;
    }
}