package com.hackathon.eot.model.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@Table(name = "article")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class ArticleEntity extends AuditingFields {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserAccount user;

    @ManyToOne
    @JoinColumn(name = "planner_id")
    private PlannerEntity planner;

    @Column(name = "title")
    private String title;

    @Column(name = "content")
    private String content;

    private ArticleEntity(UserAccount userAccount, PlannerEntity plannerEntity, String title, String content) {
        this.user = userAccount;
        this.planner = plannerEntity;
        this.title = title;
        this.content = content;
    }

    public static ArticleEntity of(UserAccount userAccount, PlannerEntity plannerEntity, String title, String content) {
        return new ArticleEntity(userAccount, plannerEntity, title, content);
    }
}
