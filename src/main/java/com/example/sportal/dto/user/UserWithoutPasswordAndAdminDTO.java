package com.example.sportal.dto.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserWithoutPasswordAndAdminDTO {
    private long id;
    protected String username;
    protected String email;
    @JsonIgnore
    protected boolean isActive;
}
