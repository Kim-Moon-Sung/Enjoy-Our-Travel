package com.hackathon.eot.model.dto;

import com.hackathon.eot.model.entity.ArticleEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class ArticleDto {

    private Long id;
    private User user;
    private Long plannerId;
    private String title;
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

    public static ArticleDto of(User user, Long plannerId, String title, String content) {
        return new ArticleDto(null, user, plannerId, title, content, null, null);
    }

    public static ArticleDto of(Long id, User user, Long plannerId, String title, String content, LocalDateTime createdAt, LocalDateTime modifiedAt) {
        return new ArticleDto(id, user, plannerId, title, content, createdAt, modifiedAt);
    }

    public static ArticleDto fromEntity(ArticleEntity entity) {
        return new ArticleDto(
                entity.getId(),
                User.fromEntity(entity.getUser()),
                entity.getPlanner().getId(),
                entity.getTitle(),
                entity.getContent(),
                entity.getCreatedAt(),
                entity.getModifiedAt()
        );
    }
}
