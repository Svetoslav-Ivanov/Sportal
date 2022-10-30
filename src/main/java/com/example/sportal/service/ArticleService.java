package com.example.sportal.service;

import com.example.sportal.dto.article.ArticleDTO;
import com.example.sportal.model.entity.Article;
import com.example.sportal.model.entity.Image;

import com.example.sportal.model.exception.InvalidDataException;
import com.example.sportal.model.exception.NotFoundException;
import com.example.sportal.model.exception.ServerException;
import com.example.sportal.repository.CategoryRepository;

import com.example.sportal.util.validators.ArticleValidator;
import org.apache.commons.io.FilenameUtils;
import org.modelmapper.ModelMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import java.util.stream.Collectors;

@Service
public class ArticleService extends AbstractService {
    public static final String UPLOADS = "uploads";
    public static final String IMAGES = "images";

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
        article.getImages().forEach(imageService::deleteImage);
        article.getAuthor().getArticles().remove(article);
        articleRepository.delete(article);
        return dto;
    }

    public ArticleDTO createArticle(String title, String text,
                                    long categoryId,
                                    MultipartFile[] multipartFiles,
                                    long authorId) {

        if (multipartFiles == null || multipartFiles.length == 0) {
            throw new InvalidDataException("The article must have at least one image!");
        }

        if (articleValidator.titleAndTextAreValid(title, text)) {
            Article article = new Article();
            article.setTitle(title);
            article.setText(text);
            article.setPostDate(Calendar.getInstance().getTime());
            article.setAuthor(getUserById(authorId));
            article.setDailyViews(0);
            article.setCategory(getCategoryById(categoryId));
            List<Image> images = createImages(multipartFiles, article);
            article.setImages(images);
            articleRepository.save(article);
            return modelMapper.map(article, ArticleDTO.class);
        }
        throw new InvalidDataException("Invalid data given! ");
    }

    //    TODO: @Transactional
    public ArticleDTO editArticle(long articleId, String title,
                                  String text, long categoryId,
                                  MultipartFile[] multipartFiles) {
        Article article = getArticleById(articleId);
        if (articleValidator.titleAndTextAreValid(title, text, articleId)) {
            article.setTitle(title);
            article.setText(text);
            article.setCategory(categoryRepository.getById(categoryId));
            if (multipartFiles != null && multipartFiles.length > 0) {
                article.getImages().forEach(imageService::deleteImage);
                List<Image> images = createImages(multipartFiles, article);
                article.setImages(images);
            } else {
                throw new InvalidDataException("The article must have at least one image!");
            }
        }
        return modelMapper.map(article, ArticleDTO.class);
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

    public List<ArticleDTO> searchByTitle(String text) {
        List<Article> articles = articleRepository
                .findAllByTitleContainingIgnoreCaseOrTextContainingIgnoreCase(text, text);
        if (articles.size() > 0) {
            return articles
                    .stream()
                    .map(a -> modelMapper.map(a, ArticleDTO.class))
                    .collect(Collectors.toList());
        }
        throw new NotFoundException("Articles not found!");
    }

    @Transactional
    protected List<Image> createImages(MultipartFile[] multipartFiles, Article article) {
        List<Image> images = new ArrayList<>();
        for (MultipartFile image : multipartFiles) {
            String ext = FilenameUtils.getExtension(image.getOriginalFilename());
            String name = System.nanoTime() + "." + ext;
            File file = new File(UPLOADS + File.separator + name);
            if (!file.exists()) {
                try {
                    Files.copy(image.getInputStream(), file.toPath());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                throw new ServerException("The file already exist!");
            }
            Image i = new Image(name, article);
            imageRepository.save(i);
            images.add(i);
        }
        return images;
    }
}
