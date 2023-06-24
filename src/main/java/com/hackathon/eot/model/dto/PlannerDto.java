package com.hackathon.eot.model.dto;

import com.hackathon.eot.model.entity.PlannerEntity;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class PlannerDto {

    private Long id;
    private User user;
    private String title;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

    public static PlannerDto of(User user, String title) {
        return new PlannerDto(null, user, title, null, null);
    }

    public static PlannerDto of(Long id, User user, String title, LocalDateTime createdAt, LocalDateTime modifiedAt) {
        return new PlannerDto(id, user, title, createdAt, modifiedAt);
    }

    public static PlannerDto fromEntity(PlannerEntity entity) {
        return new PlannerDto(
                entity.getId(),
                User.fromEntity(entity.getUser()),
                entity.getTitle(),
                entity.getCreatedAt(),
                entity.getModifiedAt()
        );
    }
}
