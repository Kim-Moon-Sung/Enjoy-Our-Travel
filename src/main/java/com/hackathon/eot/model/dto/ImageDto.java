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
    private String filePath;
    private String originalFileName;
    private ImageRole role;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

    public static ImageDto of(Long articleId, String filePath, String originalFileName, ImageRole role) {
        return new ImageDto(null, articleId, filePath, originalFileName, role, null, null);
    }

    public static ImageDto of(String filePath, String originalFileName, ImageRole role) {
        return new ImageDto(null, null, filePath, originalFileName, role, null, null);
    }

    public static ImageDto of(Long id, Long articleId, String filePath, String originalFileName, ImageRole role, LocalDateTime createdAt, LocalDateTime modifiedAt) {
        return new ImageDto(id, articleId, filePath, originalFileName, role, createdAt, modifiedAt);
    }

    public static ImageDto fromEntity(ImageEntity entity) {
        return new ImageDto(
                entity.getId(),
                entity.getArticle().getId(),
                entity.getFilePath(),
                entity.getOriginalFileName(),
                entity.getRole(),
                entity.getCreatedAt(),
                entity.getModifiedAt()
        );
    }
}
