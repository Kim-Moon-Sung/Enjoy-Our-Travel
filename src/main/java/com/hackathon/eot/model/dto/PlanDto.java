package com.hackathon.eot.model.dto;

import com.hackathon.eot.model.entity.PlanEntity;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class PlanDto {

    private Long id;
    private Long plannerId;
    private String place;
    private String content;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Long fee;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

    public static PlanDto of(Long plannerId, String place, String content, LocalDateTime startTime, LocalDateTime endTime, Long fee) {
        return new PlanDto(null, plannerId, place, content, startTime, endTime, fee, null, null);
    }

    public static PlanDto of(Long id, Long plannerId, String place, String content, LocalDateTime startTime, LocalDateTime endTime, Long fee, LocalDateTime createdAt, LocalDateTime modifiedAt) {
        return new PlanDto(id, plannerId, place, content, startTime, endTime, fee, createdAt, modifiedAt);
    }

    public static PlanDto fromEntity(PlanEntity entity) {
        return new PlanDto(
                entity.getId(),
                entity.getPlanner().getId(),
                entity.getPlace(),
                entity.getContent(),
                entity.getStartTime(),
                entity.getEndTime(),
                entity.getFee(),
                entity.getCreatedAt(),
                entity.getModifiedAt()
        );
    }
}
