package com.example.sportal.dto.user;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserRegisterDTO {
    protected String username;
    protected String password;
    @JsonProperty("confirm-password")
    protected String confirmPassword;
    protected String email;
}
