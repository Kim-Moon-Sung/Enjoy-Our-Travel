package com.hackathon.eot.model.dto;

import com.hackathon.eot.model.entity.BookmarkEntity;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class BookmarkDto {

    private Long id;
    private Long articleId;
    private User user;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

    public static BookmarkDto of(Long articleId, User user) {
        return new BookmarkDto(null, articleId, user, null, null);
    }

    public static BookmarkDto of(Long id, Long articleId, User user, LocalDateTime createdAt, LocalDateTime modifiedAt) {
        return new BookmarkDto(id, articleId, user, createdAt, modifiedAt);
    }

    public static BookmarkDto fromEntity(BookmarkEntity entity) {
        return new BookmarkDto(
                entity.getId(),
                entity.getArticle().getId(),
                User.fromEntity(entity.getUser()),
                entity.getCreatedAt(),
                entity.getModifiedAt()
        );
    }
}
