package com.example.sportal.repository;

import com.example.sportal.model.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;


public interface CommentRepository extends JpaRepository<Comment, Long> {
    Optional<Comment> findCommentById(long commentId);

    Optional<List<Comment>> findAllCommentsByArticleId(long articleId);
}
