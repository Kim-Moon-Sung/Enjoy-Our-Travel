package com.hackathon.eot.model.dto;

import com.hackathon.eot.model.constant.ImageRole;
import com.hackathon.eot.model.entity.ImageEntity;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class ImageDto {

    private Long id;
    private Long articleId;
    private String imageUrl;
    private ImageRole role;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

    public static ImageDto of(Long articleId, String imageUrl, ImageRole role) {
        return new ImageDto(null, articleId, imageUrl, role, null, null);
    }

    public static ImageDto of(Long id, Long articleId, String imageUrl, ImageRole role, LocalDateTime createdAt, LocalDateTime modifiedAt) {
        return new ImageDto(id, articleId, imageUrl, role, createdAt, modifiedAt);
    }

    public static ImageDto fromEntity(ImageEntity entity) {
        return new ImageDto(
                entity.getId(),
                entity.getArticle().getId(),
                entity.getImageUrl(),
                entity.getRole(),
                entity.getCreatedAt(),
                entity.getModifiedAt()
        );
    }
}
