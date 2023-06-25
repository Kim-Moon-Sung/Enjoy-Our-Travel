package com.hackathon.eot.dto.response;

import com.hackathon.eot.model.dto.CommentDto;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class CommentResponse {
    private Long id;
    private String content;
    private Long articleId;
    private String userAccountId;

    public static CommentResponse fromCommentDto(CommentDto comment) {
        return new CommentResponse(
                comment.getId(),
                comment.getContent(),
                comment.getArticleId(),
                comment.getUser().getUsername()
        );
    }
}
