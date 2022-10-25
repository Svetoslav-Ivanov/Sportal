package com.example.sportal.dto.article;

import com.example.sportal.model.entity.Comment;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Calendar;
import java.util.List;


@Getter
@Setter
@NoArgsConstructor
public class ArticleDTO extends NewArticleDTO {
    private long articleId;
    private int views = 0;
    private Calendar postDate;
    private List<Comment> comments;
}
