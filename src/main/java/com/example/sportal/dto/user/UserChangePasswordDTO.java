package com.example.sportal.dto.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserChangePasswordDTO {
    private String password;
    @JsonProperty("confirm_password")
    private String confirmPassword;
}
