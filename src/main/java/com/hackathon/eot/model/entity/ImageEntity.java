package com.hackathon.eot.model.entity;

import com.hackathon.eot.model.constant.ImageRole;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@Table(name = "image")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class ImageEntity extends AuditingFields {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "article_id")
    private ArticleEntity article;

    @Column(name = "image_url")
    private String imageUrl;

    @Column(name = "role")
    @Enumerated(EnumType.STRING)
    private ImageRole role;

    private ImageEntity(ArticleEntity articleEntity, String imageUrl, ImageRole imageRole) {
        this.article = articleEntity;
        this.imageUrl = imageUrl;
        this.role = imageRole;
    }

    public static ImageEntity of(ArticleEntity articleEntity, String imageUrl, ImageRole imageRole) {
        return new ImageEntity(articleEntity, imageUrl, imageRole);
    }
}
