package com.example.sportal;

import com.example.sportal.util.validators.ArticleValidator;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class SportalApplication {

    public static void main(String[] args) {
        SpringApplication.run(SportalApplication.class, args);
    }

}
