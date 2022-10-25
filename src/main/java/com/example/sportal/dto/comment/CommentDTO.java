package com.example.sportal.dto.comment;

import com.example.sportal.model.entity.User;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class CommentDTO extends EditCommentDTO {
    @JsonProperty ("author-id")
    protected long authorId;
    @JsonProperty ("article-id")
    protected long articleId;
    @JsonProperty ("liked-by")
    protected List<User> likedBy;
    @JsonProperty ("disliked-by")
    protected List<User> dislikedBy;
    @JsonProperty ("disliked-by")
    protected List<CommentDTO> children;
}
