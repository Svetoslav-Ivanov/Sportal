package com.example.sportal.util.validators;

import com.example.sportal.model.entity.User;
import com.example.sportal.model.exception.DataAlreadyExistException;
import com.example.sportal.model.exception.InvalidDataException;
import com.example.sportal.repository.UserRepository;
import org.passay.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.regex.Pattern;

@Component
public class UserValidator {


    @Autowired
    protected UserRepository userRepository;

    public boolean emailIsValid(String emailAddress) {
        String regexPattern = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@"
                + "[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";
        if (!Pattern.compile(regexPattern)
                .matcher(emailAddress)
                .matches()) {
            throw new InvalidDataException("Invalid email address!");
        }
        return true;
    }

    public boolean passwordsAreValid(String newPassword, String confirmPassword) {
        if (newPassword == null || newPassword.isEmpty() || newPassword.isBlank()) {
            throw new InvalidDataException("Password cannot be empty!");
        }
        if (confirmPassword == null || confirmPassword.isEmpty() || confirmPassword.isBlank()) {
            throw new InvalidDataException("Password confirmation cannot be empty!");
        }
        if (newPassword.length() != confirmPassword.length() || !newPassword.equals(confirmPassword)) {
            throw new InvalidDataException("Passwords doesn`t match");
        }

        PasswordValidator passwordValidator = new PasswordValidator(
                new LengthRule(8, 16),
                new WhitespaceRule(),
                new CharacterRule(EnglishCharacterData.LowerCase, 1),
                new CharacterRule(EnglishCharacterData.UpperCase, 1),
                new CharacterRule(EnglishCharacterData.Digit, 1),
                new CharacterRule(EnglishCharacterData.Special, 1),
                new IllegalCharacterRule(new char[]{'@'})

        );
        PasswordData passwordData = new PasswordData(newPassword);
        RuleResult ruleResult = passwordValidator.validate(passwordData);

        if (!ruleResult.isValid()) {
            throw new InvalidDataException("Invalid Password: " + passwordValidator.getMessages(ruleResult));
        }
        return true;
    }

    public boolean usernameIsValid(String username) {
        PasswordValidator validator = new PasswordValidator(
                new LengthRule(5, 25),
                new IllegalCharacterRule(new char[]{
                        '@', '!', '#', '$', '%', '^', '&',
                        '*', '(', ')', '+', '=', '\\', '\'',
                        '\"', ':', ',', '{', '}', '[', ']', '<',
                        '>', '?', '/', '|', '~', '`'})
        );
        PasswordData dataToValidate = new PasswordData(username);
        RuleResult ruleResult = validator.validate(dataToValidate);

        if (!ruleResult.isValid()) {
            System.out.println(ruleResult.getDetails().toString());
            throw new InvalidDataException("Invalid username!");

        }
        return true;
    }

    public boolean usernameIsNotForeign(User user, String username) {
        Optional<User> u = userRepository.findByUsername(username);
        if (u.isPresent()) {
           if (u.get().getId() != user.getId()){
               throw new DataAlreadyExistException("This username already exists");
           }
        }
        return true;
    }

    public boolean emailIsNotForeign(User user, String email) {
        Optional<User> u = userRepository.findUserByEmail(email);
        if (u.isPresent()) {
            if (u.get().getId() != user.getId()){
                throw new DataAlreadyExistException("This email already exists");
            }
        }
        return true;
    }
}
