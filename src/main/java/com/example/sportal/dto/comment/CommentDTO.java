package com.example.sportal.dto.comment;

import com.example.sportal.dto.user.UserWithoutPasswordAndActiveAndAdminDTO;
import com.example.sportal.model.entity.User;
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
    protected long authorId;
    protected long articleId;
    protected String text;
    protected List<UserWithoutPasswordAndActiveAndAdminDTO> likedBy;
    protected List<UserWithoutPasswordAndActiveAndAdminDTO> dislikedBy;
    protected List<CommentDTO> answers;
}
