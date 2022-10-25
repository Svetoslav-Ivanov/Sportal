package com.example.sportal.dto.article;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class EditArticleDTO extends NewArticleDTO{
    private long articleId;
}
