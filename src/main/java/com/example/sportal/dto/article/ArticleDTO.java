package com.example.sportal.dto.article;

import com.example.sportal.dto.comment.CommentDTO;
import com.example.sportal.dto.user.UserWithoutPasswordAndAdminDTO;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Calendar;
import java.util.List;


@Getter
@Setter
@NoArgsConstructor
public class ArticleDTO extends NewArticleDTO {
    @JsonProperty("article_id")
    private long articleId;
    private int views = 0;
    @JsonProperty("post_date")
    private Calendar postDate;
    private List<CommentDTO> comments;
    private UserWithoutPasswordAndAdminDTO author;
}
