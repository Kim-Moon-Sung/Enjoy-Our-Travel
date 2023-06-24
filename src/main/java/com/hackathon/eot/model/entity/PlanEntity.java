package com.hackathon.eot.model.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Table(name = "plan")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class PlanEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "planner_id")
    private PlannerEntity planner;

    @Column(name = "place")
    private String place;

    @Column(name = "conetent")
    private String content;

    @DateTimeFormat(iso = ISO.DATE_TIME)
    @Column(name = "start_time")
    private LocalDateTime startTime;

    @DateTimeFormat(iso = ISO.DATE_TIME)
    @Column(name = "end_time")
    private LocalDateTime endTime;

    @Column(name = "fee")
    private Long fee;

    private PlanEntity(PlannerEntity plannerEntity, String place, String content, LocalDateTime startTime, LocalDateTime endTime, Long fee) {
        this.planner = plannerEntity;
        this.place = place;
        this.content = content;
        this.startTime = startTime;
        this.endTime = endTime;
        this.fee = fee;
    }

    public static PlanEntity of(PlannerEntity plannerEntity, String place, String content, LocalDateTime startTime, LocalDateTime endTime, Long fee) {
        return new PlanEntity(plannerEntity, place, content, startTime, endTime, fee);
    }
}
