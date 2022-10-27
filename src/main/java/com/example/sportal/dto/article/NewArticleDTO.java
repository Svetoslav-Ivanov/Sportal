package com.example.sportal.dto.article;

import com.example.sportal.model.entity.Image;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class NewArticleDTO {
    @JsonProperty("category-id")
    private int categoryId;
    private String title;
    private String text;
    private Image image;
}
