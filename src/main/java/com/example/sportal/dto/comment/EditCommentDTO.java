package com.example.sportal.dto.comment;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class EditCommentDTO extends NewCommentDTO{
    protected long id;
}
