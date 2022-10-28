package com.example.sportal.service;

import com.example.sportal.dto.user.*;
import com.example.sportal.model.entity.ResetPasswordLink;
import com.example.sportal.model.entity.User;
import com.example.sportal.model.exception.*;
import com.example.sportal.model.exception.AuthenticationException;
import com.example.sportal.model.exception.DataAlreadyExistException;
import com.example.sportal.model.exception.InvalidDataException;
import com.example.sportal.model.exception.RessetPasswordException;
import com.example.sportal.repository.ResetPasswordLinkRepository;
import com.example.sportal.util.EmailSender;
import com.example.sportal.util.validators.UserValidator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.*;

@Service
public class UserService extends AbstractService {

    public static final int RESET_PASSWORD_DURATION = 20;
    public static final String RESET_PASSWORD_MESSAGE = "Check your email address. If it exist in our system, " +
            "you will receive a reset password link.";
    private static final String HOST = "http://172.20.10.11:8080/users/reset-password?id=";
    @Autowired
    private BCryptPasswordEncoder encoder;
    @Autowired
    private ResetPasswordLinkRepository resetPasswordLinkRepository;
    @Autowired
    private EmailSender emailer;
    @Autowired
    private UserValidator userValidator;

    public UserWithoutPasswordAndActiveAndAdminDTO getById(long id) {
        User user = getUserById(id);
        return modelMapper.map(user, UserWithoutPasswordAndActiveAndAdminDTO.class);
    }

    public UserWithoutPasswordAndActiveAndAdminDTO register(UserRegisterDTO userRegisterDTO) {
        if (!userValidator.emailIsValid(userRegisterDTO.getEmail())) {
            throw new InvalidDataException("Invalid email given!");
        }
        if (userRepository.existsByEmail(userRegisterDTO.getEmail())) {
            throw new DataAlreadyExistException("This email already exists!");
        }
        if (!userValidator.usernameIsValid(userRegisterDTO.getUsername())) {
            throw new InvalidDataException("Invalid username given!");
        }
        if (userRepository.existsByUsername(userRegisterDTO.getUsername())) {
            throw new DataAlreadyExistException("This username already exists!");
        }
        if (!userValidator.passwordsAreValid(userRegisterDTO.getPassword(), userRegisterDTO.getConfirmPassword())) {
            throw new InvalidDataException("Invalid passwords given!");
        }

        User user = modelMapper.map(userRegisterDTO, User.class);
        user.setActive(true);
        user.setAdmin(false);
        user.setPassword(encoder.encode(userRegisterDTO.getPassword()));
        userRepository.save(user);
        return modelMapper.map(user, UserWithoutPasswordAndActiveAndAdminDTO.class);
    }

    @Transactional
    public UserWithoutPasswordAndActiveAndAdminDTO edit(long userId, @RequestBody UserEditDTO dto) {
        User user = getUserById(userId);
        if (userValidator.usernameIsValid(dto.getUsername())
                && userValidator.usernameIsNotForeign(user, dto.getUsername())) {
            user.setUsername(dto.getUsername());
        }
        if (userValidator.emailIsValid(dto.getEmail())
                && userValidator.emailIsNotForeign(user, dto.getEmail())) {
            user.setEmail(dto.getEmail());
        }
        if (dto.getPassword() != null || dto.getNewPassword() != null || dto.getConfirmPassword() != null) {
            if (userValidator.passwordsAreValid(dto.getNewPassword(), dto.getConfirmPassword())) {
                if (encoder.matches(dto.getPassword(), user.getPassword())) {
                    user.setPassword(encoder.encode(dto.getNewPassword()));
                } else {
                    throw new AuthenticationException("Wrong credentials!");
                }
            }
        }
        user.setAdmin(dto.isAdmin());
        userRepository.save(user);
        return modelMapper.map(user, UserWithoutPasswordAndActiveAndAdminDTO.class);
    }

    @Transactional
    public UserWithoutPasswordAndActiveAndAdminDTO delete(long id) {
        User user = getUserById(id);
        String deletedAt = "Deleted at " + Calendar.getInstance().getTime();
        user.setActive(false);
        user.setAdmin(false);
        user.setUsername(deletedAt);
        user.setEmail(deletedAt);
        user.setPassword(deletedAt);
        userRepository.save(user);
        return modelMapper.map(user, UserWithoutPasswordAndActiveAndAdminDTO.class);
    }

    public UserWithoutPasswordAndActiveAndAdminDTO login(UserLoginDTO dto) {
        String login = dto.getLogin().trim();
        String password = dto.getPassword().trim();
        User user = getUserByUsernameOrEmail(login, login);
        if (!encoder.matches(password, user.getPassword())) {
            throw new AuthenticationException("Wrong credentials");
        }
        return modelMapper.map(user, UserWithoutPasswordAndActiveAndAdminDTO.class);
    }

    @Transactional
    public boolean requestResetPassword(String email) {
        Optional<User> user = userRepository.findUserByEmail(email);

        if (user.isEmpty() || !user.get().isActive()) {
            throw new RessetPasswordException(RESET_PASSWORD_MESSAGE);
        }
        ResetPasswordLink link = new ResetPasswordLink();
        String resetId = createRandomURI();
        String URL = HOST + resetId;
        link.setURI(resetId);
        link.setIntendedFor(user.get());
        Calendar expiresAt = Calendar.getInstance();
        expiresAt.add(Calendar.MINUTE, RESET_PASSWORD_DURATION);
        link.setExpiresAt(expiresAt);
        String text = "Hello dear " + user.get().getUsername() + ",\n\n" +
                "Please follow the link to reset your password:\n" +
                URL + "\n\n" +
                "This link expires in 20 minutes, or when your password is reset." + "\n\n\n" +
                "Best regards,\n" +
                "Sportal team";
        String subject = "Sportal - Password recovery link";
        emailer.sendMessage(email, subject, text);
        resetPasswordLinkRepository.save(link);
        return true;
    }

    @Transactional
    public boolean resetPassword(String URI, UserChangePasswordDTO dto) {
        ResetPasswordLink link = resetPasswordLinkRepository.findById(URI)
                .orElseThrow(() -> new NotFoundException("Link not found!"));
        if (link.getExpiresAt().after(Calendar.getInstance().getTime())) {
            throw new NotFoundException("This link has been expired!");
        }
        if (!userValidator.passwordsAreValid(dto.getPassword(), dto.getConfirmPassword())) {
            throw new InvalidDataException("This password doesn`t meet our requirements!");
        }
        User user = link.getIntendedFor();
        user.setPassword(encoder.encode(dto.getPassword()));
        resetPasswordLinkRepository.delete(link);
        userRepository.save(user);
        return true;
    }

    public boolean isAdmin(long id) {
        User u = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("User not found!"));
        return u.isAdmin();
    }

    private String createRandomURI() {
        StringBuilder URI = new StringBuilder();
        for (int i = 0; i < 6; i++) {
            URI.append(new Random().nextInt(10));
        }
        return encoder.encode(URI);
    }
}