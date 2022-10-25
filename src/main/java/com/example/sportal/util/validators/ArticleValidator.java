package com.example.sportal.util.validators;

import com.example.sportal.dto.article.NewArticleDTO;
import com.example.sportal.model.exception.InvalidDataException;
import com.example.sportal.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;

public class ArticleValidator {

    public static final int MIN_TITLE_LENGTH = 20;
    public static final int MAX_TITLE_LENGTH = 200;
    public static final int MIN_TEXT_LENGTH = 200;
    public static final int MAX_TEXT_LENGTH = 20_000;

    @Autowired
    private CategoryRepository categoryRepository;

    public boolean isValidDTO(NewArticleDTO dto) {
        return titleIsValid(dto.getTitle())
                && textIsValid(dto.getText())
                && categoryRepository.existsByName(dto.getCategoryName());
    }

    public boolean titleIsValid(String title) {
        if (isEmpty(title)) {
            throw new InvalidDataException("Title cannot be empty!");
        }
        if (title.length() < MIN_TITLE_LENGTH){
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
        if (text.length() < MIN_TEXT_LENGTH){
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