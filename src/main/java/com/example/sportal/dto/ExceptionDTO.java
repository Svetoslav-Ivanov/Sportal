package com.example.sportal.dto;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class ExceptionDTO {

    private int status;
    private LocalDateTime dateTime;
    private String message;
}
