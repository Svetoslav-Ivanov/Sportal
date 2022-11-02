package com.example.sportal.dto.comment;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CommentArticleDTO extends NewCommentDTO {
    @JsonProperty("article_id")
    protected long articleId;
    @JsonProperty("daily_views")
    protected long dailyViews;
}
