package com.hackathon.eot.model.dto;

import com.hackathon.eot.model.entity.CommentEntity;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class CommentDto {

    private Long id;
    private Long articleId;
    private User user;
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

    public static CommentDto of(Long articleId, User user, String content) {
        return new CommentDto(null, articleId, user, content, null, null);
    }

    public static CommentDto of(Long id, Long articleId, User user, String content, LocalDateTime createdAt, LocalDateTime modifiedAt) {
        return new CommentDto(id, articleId, user, content, createdAt, modifiedAt);
    }

    public static CommentDto fromEntity(CommentEntity entity) {
        return new CommentDto(
                entity.getId(),
                entity.getArticle().getId(),
                User.fromEntity(entity.getUser()),
                entity.getContent(),
                entity.getCreatedAt(),
                entity.getModifiedAt()
        );
    }
}
