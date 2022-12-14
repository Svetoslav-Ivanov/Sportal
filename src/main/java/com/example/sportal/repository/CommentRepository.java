package com.example.sportal.repository;

import com.example.sportal.model.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    Optional<Comment> findCommentById(long commentId);

    List<Comment> getAllCommentsByArticleId(long articleId);

}
