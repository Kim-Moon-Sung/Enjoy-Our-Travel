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

    @Column(name = "file_path")
    private String filePath; // 파일 저장 경로

    @Column(name = "original_file_name")
    private String originalFileName; // 파일 원본명

    @Column(name = "role")
    @Enumerated(EnumType.STRING)
    private ImageRole role;

    private ImageEntity(ArticleEntity articleEntity, String filePath, String originalFileName, ImageRole imageRole) {
        this.article = articleEntity;
        this.filePath = filePath;
        this.originalFileName = originalFileName;
        this.role = imageRole;
    }

    public static ImageEntity of(String filePath, String originalFileName, ImageRole imageRole) {
        return new ImageEntity(null, filePath, originalFileName, imageRole);
    }

    public static ImageEntity of(ArticleEntity articleEntity, String filePath, String originalFileName, ImageRole imageRole) {
        return new ImageEntity(articleEntity, filePath, originalFileName, imageRole);
    }

    public void setArticle(ArticleEntity article) {
        this.article = article;

        // 게시글에 현재 파일이 존재하지 않는다면
        if(!article.getImages().contains(this))
            article.getImages().add(this);
    }
}
