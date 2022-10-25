package com.example.sportal.controller;

import com.example.sportal.dto.comment.*;
import com.example.sportal.model.exception.MethodNotAllowedException;
import com.example.sportal.model.exception.NotFoundException;
import com.example.sportal.model.exception.UserNotLoggedException;
import com.example.sportal.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/comments")
public class CommentController extends AbstractController {

    @Autowired
    private CommentService commentService;

    @PostMapping()
    @ResponseStatus(code = HttpStatus.CREATED)
    public CommentDTO commentArticle(@RequestBody CommentArticleDTO dto, HttpServletRequest request) {
        long loggedUserId = getLoggedUserId(request);
        return commentService.commentArticle(dto, loggedUserId);
    }

    @PostMapping("/{parentId}") // TODO: Ask about this
    @ResponseStatus(code = HttpStatus.CREATED)
    public CommentDTO answerComment(@RequestBody NewCommentDTO dto, @PathVariable long parentId, HttpServletRequest request) {
        long loggedUserId = getLoggedUserId(request);
        return commentService.answerComment(parentId, loggedUserId, dto);
    }

    @GetMapping("/{cid}")
    public CommentDTO getCommentById(@PathVariable long cid) {
        return commentService.getById(cid);
    }

    @PutMapping()
    public EditCommentDTO editComment(@RequestBody EditCommentDTO dto, HttpServletRequest request) {
        long loggedUserId = getLoggedAdminId(request);
        if (commentService.isOwner(dto.getId(), loggedUserId)) {
            return commentService.editComment(dto);
        }
        throw new MethodNotAllowedException("You don`t have rights to edit this comment!");
    }

    @GetMapping("/{articleId}")
    public List<CommentDTO> getAllByArticleId(@PathVariable long articleId) {
        return commentService.getAllByArticleId(articleId);
    }

    @DeleteMapping("/{commentId}")
    public String deleteComment(@PathVariable long commentId, HttpServletRequest request) {
        long loggedUserId = getLoggedUserId(request);
        if (commentService.isOwner(commentId, loggedUserId)
                || isAdmin(request.getSession())) {
            if (commentService.deleteComment(commentId)) {
                return "Comment deleted succsesfully";
            } else {
                throw new NotFoundException("Comment not found!");
            }
        }
        throw new MethodNotAllowedException("You don`t have rights to edit this comment!");
    }

    @PostMapping("/{commentId}/like")
    public int like(@PathVariable long commentId, HttpServletRequest request) {
        long loggedUserId = getLoggedUserId(request);
        return commentService.likeComment(commentId, loggedUserId);
    }

    @PostMapping("/{commentId}/dislike")
    public int dislike(@PathVariable long commentId, HttpServletRequest request) {
        long loggedUserId = getLoggedUserId(request);
        return commentService.dislikeComment(commentId, loggedUserId);
    }
}
