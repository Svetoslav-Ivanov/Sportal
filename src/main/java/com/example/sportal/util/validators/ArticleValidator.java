package com.example.sportal.util.validators;

import com.example.sportal.dto.article.NewArticleDTO;
import com.example.sportal.model.entity.Article;
import com.example.sportal.model.exception.DataAlreadyExistException;
import com.example.sportal.model.exception.InvalidDataException;
import com.example.sportal.repository.ArticleRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

public class ArticleValidator {

    public static final int MIN_TITLE_LENGTH = 10;
    public static final int MAX_TITLE_LENGTH = 200;
    public static final int MIN_TEXT_LENGTH = 200;
    public static final int MAX_TEXT_LENGTH = 20_000;

    @Autowired
    private ArticleRepository articleRepository;

    public boolean titleAndTextAreValid(String title, String text, long articleId) {
        Article currentArticle = articleRepository.getById(articleId);
        Optional<Article> existingByTitle = articleRepository.findByTitle(title);
        if (existingByTitle.isPresent()) {
            if (currentArticle.getId() != existingByTitle.get().getId()) {
                throw new DataAlreadyExistException("This title already exists!");
            }
        }
        return titleIsValid(title)
                && textIsValid(text);
    }

    public boolean titleAndTextAreValid(String title, String text) {
        if (articleRepository.existsByTitle(title)){
            throw new DataAlreadyExistException("This title already exists!");
        }
        return titleIsValid(title)
                && textIsValid(text);
    }

    public boolean titleIsValid(String title) {
        if (isEmpty(title)) {
            throw new InvalidDataException("Title cannot be empty!");
        }
        if (title.length() < MIN_TITLE_LENGTH) {
            throw new InvalidDataException("Title too short!");
        }
        if (title.length() > MAX_TITLE_LENGTH) {
            throw new InvalidDataException("Title too large!");
        }
        return true;
    }

    public boolean textIsValid(String text) {
        if (isEmpty(text)) {
            throw new InvalidDataException("Text cannot be empty!");
        }
        if (text.length() < MIN_TEXT_LENGTH) {
            throw new InvalidDataException("Text too short!");
        }
        if (text.length() > MAX_TEXT_LENGTH) {
            throw new InvalidDataException("Text too large!");
        }
        return true;
    }

    private boolean isEmpty(String text) {
        return text == null || text.isBlank();
    }
}
