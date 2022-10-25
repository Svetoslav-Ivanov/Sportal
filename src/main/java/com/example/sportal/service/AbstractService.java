package com.example.sportal.service;

import com.example.sportal.model.entity.Article;
import com.example.sportal.model.entity.Category;
import com.example.sportal.model.entity.Comment;
import com.example.sportal.model.entity.User;
import com.example.sportal.model.exception.NotFoundException;
import com.example.sportal.model.exception.AuthenticationException;
import com.example.sportal.repository.ArticleRepository;
import com.example.sportal.repository.CategoryRepository;
import com.example.sportal.repository.CommentRepository;
import com.example.sportal.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;


@Service
public abstract class AbstractService {
    @Autowired
    protected UserRepository userRepository;
    @Autowired
    protected CommentRepository commentRepository;

    @Autowired
    protected ModelMapper modelMapper;

    @Autowired
    protected ArticleRepository articleRepository;

    @Autowired
    protected CategoryRepository categoryRepository;
    @Autowired
    protected JdbcTemplate jdbcTemplate;

    protected User getUserByUsernameOrEmail(String username, String email) {
        return userRepository
                .findUserByUsernameOrEmail(username, email)
                .orElseThrow(() -> new AuthenticationException("Wrong credentials!"));
    }

    protected User getUserById(long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("User not found!"));
    }

    protected Comment getCommentById(long id) {
        return commentRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Comment not found!"));
    }

    protected Article getArticleById(long id) {
        return articleRepository.findById(id).
                orElseThrow(() -> new NotFoundException("Article not found!"));
    }

    protected Category getCategoryById(long id) {
        return categoryRepository
                .findById(id)
                .orElseThrow(() -> new NotFoundException("Category not found!"));
    }


}
