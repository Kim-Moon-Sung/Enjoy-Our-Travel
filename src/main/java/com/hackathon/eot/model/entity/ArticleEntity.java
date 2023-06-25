package com.hackathon.eot.model.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Table(name = "article")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class ArticleEntity extends AuditingFields {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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

    @OneToMany(mappedBy = "article",
            cascade = {CascadeType.PERSIST, CascadeType.REMOVE},
            orphanRemoval = true)
    private List<ImageEntity> images = new ArrayList<>();

    private ArticleEntity(UserAccount userAccount, PlannerEntity plannerEntity, String title, String content) {
        this.user = userAccount;
        this.planner = plannerEntity;
        this.title = title;
        this.content = content;
    }

    public static ArticleEntity of(UserAccount userAccount, String title, String content) {
        return new ArticleEntity(userAccount, null, title, content);
    }

    public static ArticleEntity of(UserAccount userAccount, PlannerEntity plannerEntity, String title, String content) {
        return new ArticleEntity(userAccount, plannerEntity, title, content);
    }

    public void addImage(ImageEntity image) {
        this.images.add(image);

        // 게시글에 이미지 파일이 저장되어 있지 않는 경우
        if(image.getArticle() != this)
            image.setArticle(this);
    }

    public void setPlanner(PlannerEntity planner) {
        this.planner = planner;

        if(!planner.getArticles().contains(this))
            planner.getArticles().add(this);
    }
}
