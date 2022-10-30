package com.example.sportal.service;

import com.example.sportal.model.entity.Image;
import com.example.sportal.repository.ImageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;

import static com.example.sportal.service.ArticleService.UPLOADS;

@Service
public class ImageService {
    @Autowired
    private ImageRepository imageRepository;

    public void deleteImage(Image image) {
        File file = new File(UPLOADS + File.separator + image.getURI());
        imageRepository.delete(image);
        file.delete();
    }
}
