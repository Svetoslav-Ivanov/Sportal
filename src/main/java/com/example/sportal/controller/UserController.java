package com.example.sportal.controller;

import com.example.sportal.dto.user.*;
import com.example.sportal.model.exception.*;
import com.example.sportal.service.ResetPasswordLinkService;
import com.example.sportal.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import static com.example.sportal.service.UserService.RESET_PASSWORD_MESSAGE;

@RestController
@RequestMapping(value = "/users")
public class UserController extends AbstractController {
    @Autowired
    private UserService userService;
    @Autowired
    private ResetPasswordLinkService resetPasswordLinkService;

    @GetMapping("/{uid}")
    public UserWithoutPasswordAndActiveAndAdminDTO getById(@PathVariable long uid) {
        return userService.getById(uid);
    }

    @PostMapping("/singup")
    public UserWithoutPasswordAndActiveAndAdminDTO register(@RequestBody UserRegisterDTO userRegisterDTO, HttpSession session) {
        if (session.getAttribute(USER_ID) == null) {
            return userService.register(userRegisterDTO);
        }
        throw new MethodNotAllowedException("You are already logged in!");
    }

    @PutMapping("/login")
    public UserWithoutPasswordAndActiveAndAdminDTO login(@RequestBody UserLoginDTO dto, HttpSession session, HttpServletRequest request) {
        if (session.getAttribute(USER_ID) != null) {
            throw new InvalidOperationException("You are already logged in!");
        }
        UserWithoutPasswordAndActiveAndAdminDTO loggedDto = userService.login(dto);
        if (loggedDto != null) {
            session.setAttribute(REMOTE_ADDRESS, request.getRemoteAddr());
            session.setAttribute(USER_ID, loggedDto.getId());
            if (userService.isAdmin(loggedDto.getId())) {
                session.setAttribute(IS_ADMIN, TRUE);
            } else {
                session.setAttribute(IS_ADMIN, FALSE);
            }
            return loggedDto;
        }
        throw new AuthenticationException("Wrong credentials!");
    }

    @PutMapping("/logout")
    public String logout(HttpSession session) {
        if (session.getAttribute(USER_ID) != null) {
            session.invalidate();
            return "Logout successfully";
        }
        throw new UserNotLoggedException("You are not logged yet!");
    }

    @PutMapping("/{uid}")
    public UserWithoutPasswordAndActiveAndAdminDTO editUser(@PathVariable long uid, @RequestBody UserEditDTO dto, HttpServletRequest request) {
        long loggedUserId = getLoggedUserId(request);
        if (uid == loggedUserId || isAdmin(request.getSession())) {
            return userService.edit(uid, dto);
        }
        throw new MethodNotAllowedException("You don`t have permission to do this action!");
    }

    @DeleteMapping("/{uid}") // TODO: Ask
    public String deleteUser(@PathVariable long uid, HttpServletRequest request) {
        long loggedUserId = getLoggedUserId(request);
        if (uid == loggedUserId || isAdmin(request.getSession())) {
            if (userService.delete(uid)) {
                if (uid == loggedUserId) {
                    request.getSession().invalidate();
                }
                return "Profile deleted successfully!";
            } else {
                throw new NotFoundException("User not found");
            }
        }
        throw new MethodNotAllowedException("You don`t have permission to do this action!");
    }


    @PostMapping("/reset-password")
    public String requestResetPassword(@RequestBody UserResetPasswordDTO dto, HttpSession session) {
        if (session.getAttribute(USER_ID) != null) {
            throw new InvalidOperationException("To reset your password, you must be logged out!");
        }
        if (!userService.requestResetPassword(dto.getEmail())) {
            throw new NotFoundException("User not found!");
        }
        return RESET_PASSWORD_MESSAGE;
    }

    @PutMapping("/reset-password") // TODO:TEST
    public String resetPassword(@RequestParam("id") String id, @RequestBody UserChangePasswordDTO dto) {
        if (!resetPasswordLinkService.existsById(id)) {
            throw new NotFoundException("Link not found!");
        }
        if (!userService.resetPassword(id, dto)) {
            throw new NotFoundException("User not found!");
        }
        return "Password changed successfully!";
    }

}