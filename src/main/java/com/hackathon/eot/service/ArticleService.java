package com.hackathon.eot.service;

import com.hackathon.eot.exception.EotApplicationException;
import com.hackathon.eot.exception.ErrorCode;
import com.hackathon.eot.model.dto.ArticleDto;
import com.hackathon.eot.model.dto.CommentDto;
import com.hackathon.eot.model.entity.ArticleEntity;
import com.hackathon.eot.model.entity.CommentEntity;
import com.hackathon.eot.model.entity.ImageEntity;
import com.hackathon.eot.model.entity.UserAccount;
import com.hackathon.eot.repository.ArticleRepository;
import com.hackathon.eot.repository.CommentRepository;
import com.hackathon.eot.repository.ImageRepository;
import com.hackathon.eot.repository.UserAccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ArticleService {

    private final UserAccountRepository userAccountRepository;
    private final ArticleRepository articleRepository;
    private final CommentRepository commentRepository;
    private final ImageRepository imageRepository;
    private final FileHandler fileHandler;

    @Transactional
    public void create(String title, String content, String userAccountId, List<MultipartFile> files) throws Exception {
        UserAccount user = userAccountRepository.findByUserAccountId(userAccountId)
                .orElseThrow(() -> new EotApplicationException(ErrorCode.USER_NOT_FOUND));

        ArticleEntity article = ArticleEntity.of(user, title, content);
        List<ImageEntity> images = fileHandler.parseFileInfo(files);

        // 파일이 존재할 때에만 처리
        if(!images.isEmpty()) {
            for(ImageEntity image : images) {
                article.addImage(image);
            }
            imageRepository.saveAll(images);
        }

        articleRepository.save(article);
    }

    public Page<ArticleDto> articles(Pageable pageable) {
        return articleRepository.findAll(pageable).map(ArticleDto::fromEntity);
    }

    public ArticleDto article(Long articleId){
        return articleRepository.findById(articleId)
                .map(ArticleDto::fromEntity)
                .orElseThrow(() -> new EotApplicationException(ErrorCode.POST_NOT_FOUND));
    }

    public Page<CommentDto> comments(Long articleId, Pageable pageable) {
        ArticleEntity article = articleRepository.findById(articleId)
                .orElseThrow(() -> new EotApplicationException(ErrorCode.POST_NOT_FOUND));
        return commentRepository.findAllByArticle(article, pageable).map(CommentDto::fromEntity);
    }

    @Transactional
    public void comment(Long articleId, String userAccountId, String content) {
        ArticleEntity article = articleRepository.findById(articleId).orElseThrow(
                () -> new EotApplicationException(ErrorCode.POST_NOT_FOUND));
        UserAccount user = userAccountRepository.findByUserAccountId(userAccountId)
                .orElseThrow(() -> new EotApplicationException(ErrorCode.USER_NOT_FOUND));

        commentRepository.save(CommentEntity.of(article, user, content));
    }
}
