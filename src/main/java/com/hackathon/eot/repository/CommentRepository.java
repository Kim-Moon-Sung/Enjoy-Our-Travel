package com.hackathon.eot.repository;

import com.hackathon.eot.model.entity.ArticleEntity;
import com.hackathon.eot.model.entity.CommentEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends JpaRepository<CommentEntity, Long> {

    Page<CommentEntity> findAllByArticle(ArticleEntity articleEntity, Pageable pageable);
}
