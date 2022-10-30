package com.example.sportal.controller;

import com.example.sportal.model.exception.NotFoundException;
import lombok.SneakyThrows;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.nio.file.Files;

import static com.example.sportal.service.ArticleService.UPLOADS;

@RestController
public class ImageController {
    @SneakyThrows
    @GetMapping("/articles/images/{filePath}")
    public void downloadImageByPath(@PathVariable String filePath, HttpServletResponse response) {
        File f = new File(UPLOADS + File.separator +  filePath);
        if(!f.exists()){
            throw new NotFoundException("File doesn`t exist!");
        }
        response.setContentType(Files.probeContentType(f.toPath()));
        Files.copy(f.toPath(), response.getOutputStream());
    }
}
