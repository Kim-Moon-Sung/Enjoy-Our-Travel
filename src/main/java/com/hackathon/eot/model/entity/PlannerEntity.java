package com.hackathon.eot.model.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Table(name = "planner")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class PlannerEntity extends AuditingFields {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserAccount user;

    @Column(name = "title")
    private String title;

    @OneToMany(mappedBy = "planner")
    private List<ArticleEntity> articles = new ArrayList<>();

    private PlannerEntity(UserAccount userAccount, String title) {
        this.user = userAccount;
        this.title = title;
    }

    public static PlannerEntity of(UserAccount userAccount, String title) {
        return new PlannerEntity(userAccount, title);
    }

    public void addArticle(ArticleEntity article) {
        this.articles.add(article);

        if(article.getPlanner() != this)
            article.setPlanner(this);
    }
}
