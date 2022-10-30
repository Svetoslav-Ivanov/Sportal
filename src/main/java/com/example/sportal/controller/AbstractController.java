package com.example.sportal.controller;

import com.example.sportal.dto.ExceptionDTO;
import com.example.sportal.model.exception.*;
import com.example.sportal.service.ResetPasswordLinkService;
import com.example.sportal.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;


import java.sql.SQLException;
import java.time.LocalDateTime;

public abstract class AbstractController {
    public static final String USER_ID = "USER_ID";
    public static final String IS_ADMIN = "IS_ADMIN";
    public static final String REMOTE_ADDRESS = "REMOTE_ADDRESS";
    public static final String TRUE = "TRUE";
    public static final String FALSE = "FALSE";
    public static final int INVALID_USER_ID = 0;

    @Autowired
    protected ArticleService articleService;
    @Autowired
    protected ResetPasswordLinkService resetPasswordLinkService;

    public long getLoggedUserId(HttpServletRequest request) {
        HttpSession session = request.getSession();
        String ip = request.getRemoteAddr();
        if (session.getAttribute(USER_ID) == null) {
            throw new UserNotLoggedException("To do this action, you must be logged in!");
        }
        if (!session.getAttribute(REMOTE_ADDRESS).equals(ip)) {
            session.invalidate();
        }
        return (long) session.getAttribute(USER_ID);
    }

    public long getLoggedAdminId(HttpServletRequest req) {
        HttpSession session = req.getSession();
        if (getLoggedUserId(req) != 0 && session.getAttribute(IS_ADMIN).equals(TRUE)) {
            return (long) session.getAttribute(USER_ID);
        }
        throw new MethodNotAllowedException("You don`t have permission to do this action!");
    }

    public boolean isAdmin(HttpSession session) {
        if (session.getAttribute(USER_ID) == null){
            throw new UserNotLoggedException("To do this action, you must be logged in");
        }
        return session.getAttribute(IS_ADMIN) != null && session.getAttribute(IS_ADMIN).equals(TRUE);
    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(AuthenticationException.class)
    public ExceptionDTO handleAuthenticationException(Exception e) {
        return createExceptionDTO("Authentication filed: " + e.getMessage(),
                HttpStatus.UNAUTHORIZED.value());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(BadRequestException.class)
    public ExceptionDTO handleBadRequestException(Exception e) {
        return createExceptionDTO(e.getMessage(),
                HttpStatus.BAD_REQUEST.value());
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(DataAlreadyExistException.class)
    public ExceptionDTO handleDataAlreadyExistExeption(Exception e) {
        return createExceptionDTO(e.getMessage(),
                HttpStatus.CONFLICT.value());
    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(UserNotLoggedException.class)
    public ExceptionDTO handleUserNotLoggedException(Exception e) {
        return createExceptionDTO(e.getMessage(),
                HttpStatus.UNAUTHORIZED.value());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(InvalidDataException.class)
    public ExceptionDTO handleInvalidDataException(Exception e) {
        return createExceptionDTO("Invalid data given!" + e.getMessage(),
                HttpStatus.BAD_REQUEST.value());
    }


    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    @ExceptionHandler(MethodNotAllowedException.class)
    public ExceptionDTO handleMethodNotAllowedException(Exception e) {
        return createExceptionDTO(e.getMessage(),
                HttpStatus.METHOD_NOT_ALLOWED.value());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(InvalidOperationException.class)
    public ExceptionDTO handleInvalidOperationException(Exception e) {
        return createExceptionDTO(e.getMessage(),
                HttpStatus.BAD_REQUEST.value());
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(RessetPasswordException.class)
    public ExceptionDTO handleResetPasswordException(Exception e) {
        return createExceptionDTO(e.getMessage(), HttpStatus.NOT_FOUND.value());
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NotFoundException.class)
    private ExceptionDTO handleNotFoundException(Exception e) {
        return createExceptionDTO(e.getMessage(), HttpStatus.NOT_FOUND.value());
    }

    @ResponseStatus(HttpStatus.BAD_GATEWAY)
    @ExceptionHandler(value = {Exception.class})
    public void handleServerException(Exception e) {
        e.printStackTrace();
    }

    @ResponseStatus(HttpStatus.FAILED_DEPENDENCY)
    @ExceptionHandler(SQLException.class)
    public void handleSQLException(Exception e) {
        e.printStackTrace();
    }

    private static ExceptionDTO createExceptionDTO(String msg, int httpResponseStatus) {
        ExceptionDTO dto = new ExceptionDTO();
        dto.setMessage(msg);
        dto.setStatus(httpResponseStatus);
        dto.setDateTime(LocalDateTime.now());
        return dto;
    }

}