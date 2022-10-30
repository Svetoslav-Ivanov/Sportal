package com.example.sportal.controller;

import com.example.sportal.dto.comment.*;
import com.example.sportal.model.exception.MethodNotAllowedException;
import com.example.sportal.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
public class CommentController extends AbstractController {

    @Autowired
    private CommentService commentService;

    @PostMapping("/{articleId}/comments")
    @ResponseStatus(code = HttpStatus.CREATED)
    public CommentDTO createComment(@RequestBody CommentArticleDTO dto, HttpServletRequest request) {
        long loggedUserId = getLoggedUserId(request);
        return commentService.create(dto, loggedUserId);
    }

    @PostMapping("/comments/{parentId}")
    @ResponseStatus(code = HttpStatus.CREATED)
    public CommentDTO answerComment(@RequestBody NewCommentDTO dto, @PathVariable long parentId, HttpServletRequest request) {
        long loggedUserId = getLoggedUserId(request);
        return commentService.answerComment(parentId, loggedUserId, dto);
    }

    @GetMapping("/comments/{commentId}")
    public CommentDTO getCommentById(@PathVariable long commentId) {
        return commentService.getById(commentId);
    }

    @PutMapping("/comments/{commentId}")
    public CommentDTO editComment(@RequestBody NewCommentDTO dto, @PathVariable long commentId, HttpServletRequest request) {
        long loggedUserId = getLoggedUserId(request);
        if (commentService.isOwner(commentId, loggedUserId)) {
            return commentService.editComment(commentId, dto);
        }
        throw new MethodNotAllowedException("You don`t have rights to edit this comment!");
    }

    @GetMapping("/articles/{articleId}/comments")
    public List<CommentDTO> getAllByArticleId(@PathVariable long articleId) {
        return commentService.getAllByArticleId(articleId);
    }

    @DeleteMapping("/comments/{commentId}")
    public CommentDTO deleteComment(@PathVariable long commentId, HttpServletRequest request) {
        long loggedUserId = getLoggedUserId(request);
        if (commentService.isOwner(commentId,loggedUserId) || isAdmin(request.getSession())){
            return commentService.deleteComment(commentId, loggedUserId);
        }
        throw new MethodNotAllowedException("You don`t have permission to do this action!");
    }

    @PostMapping("/comments/{commentId}/like")
    public int like(@PathVariable long commentId, HttpServletRequest request) {
        long loggedUserId = getLoggedUserId(request);
        return commentService.likeComment(commentId, loggedUserId);
    }

    @PostMapping("/comments/{commentId}/dislike")
    public int dislike(@PathVariable long commentId, HttpServletRequest request) {
        long loggedUserId = getLoggedUserId(request);
        return commentService.dislikeComment(commentId, loggedUserId);
    }
}
