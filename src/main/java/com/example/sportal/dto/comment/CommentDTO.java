package com.example.sportal.dto.comment;

import com.example.sportal.dto.user.UserWithoutPasswordAndAdminDTO;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class CommentDTO {
    protected long id;
    @JsonProperty("author_id")
    protected long authorId;
    @JsonProperty("article_id")
    protected long articleId;
    protected String text;
    @JsonProperty("likes_count")
    protected int likesCount;
    @JsonProperty("dislikes_count")
    protected int dislikesCount;
    @JsonProperty("answers_count")
    protected int answersCount;
}
