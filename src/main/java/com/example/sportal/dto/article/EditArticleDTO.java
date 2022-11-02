package com.example.sportal.dto.article;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class EditArticleDTO extends NewArticleDTO{
    @JsonProperty("article_id")
    private long articleId;
}
