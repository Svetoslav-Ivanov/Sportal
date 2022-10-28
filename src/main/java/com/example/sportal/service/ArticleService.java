package com.example.sportal.service;

import com.example.sportal.dto.article.ArticleDTO;
import com.example.sportal.dto.article.EditArticleDTO;
import com.example.sportal.dto.article.NewArticleDTO;
import com.example.sportal.dto.article.SearchArticleDTO;
import com.example.sportal.model.entity.Article;
import com.example.sportal.model.exception.InvalidDataException;
import com.example.sportal.model.exception.NotFoundException;
import com.example.sportal.repository.CategoryRepository;

import com.example.sportal.util.validators.ArticleValidator;
import org.modelmapper.ModelMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Calendar;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ArticleService extends AbstractService {
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private ArticleValidator articleValidator;


    @Transactional
    public ArticleDTO getById(long id) {
        Article article = getArticleById(id);
        article.setViews(article.getViews() + 1);
        article.setDailyViews(article.getDailyViews() + 1);
        articleRepository.save(article);
        return modelMapper.map(article, ArticleDTO.class);
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

    @Transactional
    public ArticleDTO deleteById(long id) {
        Article article = getArticleById(id);
        ArticleDTO dto = modelMapper.map(article, ArticleDTO.class);
        article.getAuthor().getArticles().remove(article);
        articleRepository.delete(article);
        return dto;
    }

    public ArticleDTO createArticle(NewArticleDTO dto, long authorId) {
        if (articleValidator.isValidDTO(dto)) { // TODO: Check for existing title
            Article article = new Article();
            article.setTitle(dto.getTitle());
            article.setText(dto.getText());
            article.setPostDate(Calendar.getInstance().getTime());
            article.setAuthor(getUserById(authorId));
            article.setDailyViews(0);
            article.setCategory(getCategoryById(dto.getCategoryId()));
            //TODO: images
            articleRepository.save(article);
            return modelMapper.map(article, ArticleDTO.class);
        }
        throw new InvalidDataException("Invalid data given! ");
    }

    @Transactional
    public ArticleDTO editArticle(EditArticleDTO dto) {
        Article article = getArticleById(dto.getArticleId());
        if (articleValidator.isValidDTO(dto)) {
            article.setTitle(dto.getTitle());
            article.setText(dto.getText());
            article.setCategory(categoryRepository.getById(dto.getCategoryId()));
            //TODO: images
            articleRepository.save(article);
            return modelMapper.map(article, ArticleDTO.class);
        }
        return null;
    }

    public List<ArticleDTO> getAllByCategoryId(long categoryId) {
        if (!categoryRepository.existsById(categoryId)) {
            throw new NotFoundException("Category not found!");
        }
        List<Article> articles = articleRepository
                .findAllByCategoryId(categoryId)
                .orElseThrow(() -> new NotFoundException("No articles"));
        return articles
                .stream()
                .map(a -> modelMapper.map(a, ArticleDTO.class))
                .collect(Collectors.toList());
    }

    @Transactional
    public void clearDailyViews() {
        List<Article> articles = articleRepository.findAll();
        articles.forEach(a -> {
            a.setViews(0);
            articleRepository.save(a);
        });
    }

    public List<ArticleDTO> searchByTitle(SearchArticleDTO dto) {
        List<Article> articles = articleRepository
                .findAllByTitleContainingIgnoreCaseOrTextContainingIgnoreCase(dto.getTitle(),dto.getTitle());
        if (articles.size() > 0) {
            return articles
                    .stream()
                    .map(a -> modelMapper.map(a, ArticleDTO.class))
                    .collect(Collectors.toList());
        }
        throw new NotFoundException("Articles not found!");
    }
}