package com.example.sportal.service;

import com.example.sportal.dto.comment.*;
import com.example.sportal.model.entity.Article;
import com.example.sportal.model.entity.Comment;
import com.example.sportal.model.entity.User;
import com.example.sportal.model.exception.*;
import com.example.sportal.repository.ArticleRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class CommentService extends AbstractService {
    private static final int MAX_COMMENT_LENGTH = 2_000;

    public CommentDTO getById(long commentId) {
        return modelMapper.map(getCommentById(commentId), CommentDTO.class);
    }

    public CommentDTO editComment(EditCommentDTO dto) {
        Comment comment = getCommentById(dto.getId());
        if (textIdValid(dto.getText())) {
            comment.setText(dto.getText());
            commentRepository.save(comment);
            return modelMapper.map(comment, CommentDTO.class);
        }
        throw new MethodNotAllowedException("You don`t have a permission to edit this comment!");
    }

    public List<CommentDTO> getAllByArticleId(long articleId) {
        List<Comment> comments = commentRepository
                .findAllCommentsByArticleId(articleId)
                .orElseThrow(() -> new NotFoundException("Article not found!"));
        return comments
                .stream()
                .map(c -> modelMapper.map(c, CommentDTO.class))
                .collect(Collectors.toList());
    }

    public CommentDTO commentArticle(CommentArticleDTO dto, long authorId) {
        if (textIdValid(dto.getText())) {
            Comment comment = new Comment();
            Article article = getArticleById(dto.getArticleId());
            User author = getUserById(authorId);
            comment.setText(dto.getText());
            comment.setArticle(article);
            comment.setAuthor(author);
            comment.setPostDate(Calendar.getInstance().getTime());
            commentRepository.save(comment);
            return modelMapper.map(comment, CommentDTO.class);
        }
        throw new InvalidDataException("Invalid data given!");
    }

    private boolean textIdValid(String text) {
        if (text != null && !text.isBlank() && text.length() <= MAX_COMMENT_LENGTH) {
            return true;
        }
        throw new InvalidDataException("Invalid text!");
    }

    public CommentDTO answerComment(long parentId, long authorId, NewCommentDTO dto) {
        if (textIdValid(dto.getText())) {
            Comment parent = getCommentById(parentId);
            Comment child = modelMapper.map(dto, Comment.class);
            child.setPostDate(Calendar.getInstance().getTime());
            parent.getAnswers().add(child);
            commentRepository.save(child);
            commentRepository.save(parent);
            return modelMapper.map(child, CommentDTO.class);
        }
        throw new BadRequestException("This action cannot be completed!");
    }

    public boolean deleteComment(long commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new NotFoundException("Comment not found!"));
        commentRepository.delete(comment);
        return true;
    }

    public int likeComment(long commentId, long userId) {
        Comment comment = getCommentById(commentId);
        User user = getUserById(userId);
        user.getDislikes().remove(comment);
        if (user.getLikes().contains(comment)) {
            user.getLikes().remove(comment);
        } else {
            user.getLikes().add(comment);
        }
        commentRepository.save(comment);
        return comment.getLikedBy().size();
    }

    public int dislikeComment(long userId, long commentId) {
        Comment comment = getCommentById(commentId);
        User user = getUserById(userId);
        user.getLikes().remove(comment);
        if (user.getDislikes().contains(comment)) {
            user.getDislikes().remove(comment);
        } else {
            user.getDislikes().add(comment);
        }
        commentRepository.save(comment);
        return comment.getLikedBy().size();
    }

    public boolean isOwner(long commentId, long userId) {
        Comment comment = commentRepository.findCommentById(commentId).orElseThrow(() -> new NotFoundException("User not found"));
        return comment.getAuthor().getId() == userId;
    }

}
