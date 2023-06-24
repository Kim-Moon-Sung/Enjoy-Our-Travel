package com.hackathon.eot.model.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@Table(name = "article_comment")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class CommentEntity extends AuditingFields {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "article_id")
    private ArticleEntity article;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserAccount user;

    @Column(name = "content")
    private String content;

    private CommentEntity(ArticleEntity articleEntity, UserAccount userAccount, String content) {
        this.article = articleEntity;
        this.user = userAccount;
        this.content = content;
    }

    public static CommentEntity of(ArticleEntity articleEntity, UserAccount userAccount, String content) {
        return new CommentEntity(articleEntity, userAccount, content);
    }
}
