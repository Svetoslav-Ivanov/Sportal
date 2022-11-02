package com.example.sportal.service;

import com.example.sportal.dto.comment.*;
import com.example.sportal.dto.user.UserWithoutPasswordAndAdminDTO;
import com.example.sportal.model.entity.Article;
import com.example.sportal.model.entity.Comment;
import com.example.sportal.model.entity.User;
import com.example.sportal.model.exception.*;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class CommentService extends AbstractService {
    private static final int MAX_COMMENT_LENGTH = 2_000;

    public CommentDTO getById(long commentId) {
        Comment comment = getCommentById(commentId);
        if (!comment.getAuthor().isActive()) {
            throw new NotFoundException("Comment not found!");
        }
        CommentDTO dto = modelMapper.map(comment, CommentDTO.class);
        dto.setLikesCount(comment.getLikedBy().size());
        dto.setDislikesCount(comment.getDislikedBy().size());
        dto.setAnswersCount(comment.getAnswers().size());
        return dto;
    }

    @Transactional
    public CommentDTO editComment(long commentId, NewCommentDTO dto) {
        Comment comment = getCommentById(commentId);
        if (textIsValid(dto.getText())) {
            comment.setText(dto.getText());
            commentRepository.save(comment);
            return modelMapper.map(comment, CommentDTO.class);
        }
        throw new MethodNotAllowedException("You don`t have a permission to edit this comment!");
    }

    public List<CommentDTO> getAllByArticleId(long articleId) {
        List<Comment> comments = commentRepository
                .getAllCommentsByArticleId(articleId);
        return comments
                .stream()
                .filter(c -> c.getAuthor().isActive())
                .map(c -> {
                   CommentDTO dto = modelMapper.map(c, CommentDTO.class);
                   dto.setLikesCount(c.getLikedBy().size());
                   dto.setDislikesCount(c.getDislikedBy().size());
                   dto.setAnswersCount(c.getAnswers().size());
                   return dto;
                })
                .collect(Collectors.toList());
    }

    @Transactional
    public CommentDTO create(CommentArticleDTO dto, long authorId) {
        if (dto.getText() == null || dto.getText().isBlank()) {
            throw new InvalidDataException("Invalid comment text!");
        }
        if (textIsValid(dto.getText())) {
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

    @Transactional
    public CommentDTO answerComment(long parentId, long authorId, NewCommentDTO dto) {
        if (textIsValid(dto.getText())) {
            Comment parent = getCommentById(parentId);
            Comment child = modelMapper.map(dto, Comment.class);
            User author = getUserById(authorId);
            child.setPostDate(Calendar.getInstance().getTime());
            child.setAuthor(author);
            child.setArticle(parent.getArticle());
            child.setParent(parent);
            parent.getAnswers().add(child);
            commentRepository.save(child); // TODO: ASK
            commentRepository.save(parent);
            return modelMapper.map(child, CommentDTO.class);
        }
        throw new BadRequestException("This action cannot be completed!");
    }

    @Transactional
    public CommentDTO deleteComment(long commentId, long userId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new NotFoundException("Comment not found!"));
        if (isOwner(commentId, userId) || getUserById(userId).isAdmin()) {
            CommentDTO dto = modelMapper.map(comment, CommentDTO.class);
            if (comment.getParent() != null) {
                comment.getParent().getAnswers().remove(comment);
            }
            if (comment.getLikedBy() != null) {
                comment.getLikedBy().forEach(u -> {
                    u.getLikes().remove(comment);
                });
            }
            if (comment.getDislikedBy() != null) {
                comment.getDislikedBy().forEach(u -> u.getDislikes().remove(comment));
            }
            commentRepository.delete(comment);
            return dto;
        }
        throw new MethodNotAllowedException("You don`t have permission to do this action!");
    }

    public int likeComment(long commentId, long userId) {
        Comment comment = getCommentById(commentId);
        User user = getUserById(userId);
        user.getDislikes().remove(comment);
        List<Comment> likes = user.getLikes();
        if (likes.contains(comment)) {
            likes.remove(comment);
        } else {
            likes.add(comment);
        }
        userRepository.save(user);
        return comment.getLikedBy().size();
    }

    public int dislikeComment(long userId, long commentId) {
        Comment comment = getCommentById(commentId);
        User user = getUserById(userId);
        user.getLikes().remove(comment);
        List<Comment> dislikes = user.getDislikes();
        if (dislikes.contains(comment)) {
            dislikes.remove(comment);
        } else {
            dislikes.add(comment);
        }
        userRepository.save(user);
        return comment.getDislikedBy().size();
    }

    public boolean isOwner(long commentId, long userId) {
        Comment comment = commentRepository
                .findCommentById(commentId)
                .orElseThrow(() -> new NotFoundException("Comment not found"));
        return comment.getAuthor().getId() == userId;
    }


    private boolean textIsValid(String text) {
        if (text != null && !text.isBlank() && text.length() <= MAX_COMMENT_LENGTH) {
            return true;
        }
        throw new InvalidDataException("Invalid text!");
    }

    public List<UserWithoutPasswordAndAdminDTO> getLikedBy(long commentId) {
        return getCommentById(commentId)
                .getLikedBy()
                .stream()
                .map(u -> modelMapper.map(u, UserWithoutPasswordAndAdminDTO.class))
                .collect(Collectors.toList());
    }

    public List<UserWithoutPasswordAndAdminDTO> getDislikedBy(long commentId) {
        return getCommentById(commentId)
                .getDislikedBy()
                .stream()
                .map(u -> modelMapper.map(u, UserWithoutPasswordAndAdminDTO.class))
                .collect(Collectors.toList());
    }

    public List<CommentDTO> getReplies(long commentId) {
        return getCommentById(commentId)
                .getAnswers()
                .stream().map(c -> modelMapper.map(c, CommentDTO.class))
                .collect(Collectors.toList());

    }
}
