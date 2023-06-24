package com.hackathon.eot.model.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@Table(name = "bookmark")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class BookmarkEntity extends AuditingFields {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "article_id")
    private ArticleEntity article;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserAccount user;

    private BookmarkEntity(ArticleEntity articleEntity, UserAccount userAccount) {
        this.article = articleEntity;
        this.user = userAccount;
    }

    public static BookmarkEntity of(ArticleEntity articleEntity, UserAccount userAccount) {
        return new BookmarkEntity(articleEntity, userAccount);
    }
}
