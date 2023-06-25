package com.hackathon.eot.service;

import com.hackathon.eot.exception.EotApplicationException;
import com.hackathon.eot.exception.ErrorCode;
import com.hackathon.eot.model.entity.ArticleEntity;
import com.hackathon.eot.model.entity.UserAccount;
import com.hackathon.eot.repository.ArticleRepository;
import com.hackathon.eot.repository.CommentRepository;
import com.hackathon.eot.repository.ImageRepository;
import com.hackathon.eot.repository.UserAccountRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@DisplayName("비즈니스 로직 - 게시글 서비스")
@SpringBootTest
class ArticleServiceTest {

    @Autowired private ArticleService articleService;

    @MockBean private UserAccountRepository userAccountRepository;
    @MockBean private ArticleRepository articleRepository;
    @MockBean private ImageRepository imageRepository;
    @MockBean private FileHandler fileHandler;
    @MockBean private CommentRepository commentRepository;

    @DisplayName("게시글 작성 - 성공")
    @Test
    public void post_article_success() throws Exception {
        // given
        String title = "test title";
        String content = "test content";
        String userAccountId = "testId";

        // when
        when(userAccountRepository.findByUserAccountId(userAccountId)).thenReturn(Optional.of(mock(UserAccount.class)));
        when(fileHandler.parseFileInfo(anyList())).thenReturn(List.of());

        // then
        Assertions.assertDoesNotThrow(() -> articleService.create(title, content, userAccountId, List.of()));
    }

    @DisplayName("게시글 작성 - 게시글 작성 시 요청하는 유저가 없는 경우")
    @Test
    public void post_article_user_not_found() throws Exception {
        // given
        String title = "test title";
        String content = "test content";
        String userAccountId = "testId";

        // when
        when(userAccountRepository.findByUserAccountId(userAccountId)).thenReturn(Optional.empty());

        // then
        EotApplicationException e = assertThrows(EotApplicationException.class, () -> articleService.create(title, content, userAccountId, List.of()));
        Assertions.assertEquals(ErrorCode.USER_NOT_FOUND, e.getErrorCode());
    }

    @DisplayName("댓글 작성 - 성공")
    @Test
    public void post_comment_success() {
        // given
        Long articleId = 1L;
        String userAccountId = "testId";
        String content = "comment test content";

        // when
        when(articleRepository.findById(articleId)).thenReturn(Optional.of(mock(ArticleEntity.class)));
        when(userAccountRepository.findByUserAccountId(userAccountId)).thenReturn(Optional.of(mock(UserAccount.class)));

        // then
        Assertions.assertDoesNotThrow(() -> articleService.comment(articleId, userAccountId, content));
    }

    @DisplayName("댓글 작성 - 댓글 작성 시 요청하는 유저가 없는 경우")
    @Test
    public void post_comment_noLogin() {
        // given
        Long articleId = 1L;
        String userAccountId = "testId";
        String content = "comment test content";

        // when
        when(articleRepository.findById(articleId)).thenReturn(Optional.of(mock(ArticleEntity.class)));

        // then
        EotApplicationException e = assertThrows(EotApplicationException.class, () -> articleService.comment(articleId, userAccountId, content));
        Assertions.assertEquals(ErrorCode.USER_NOT_FOUND, e.getErrorCode());
    }

    @DisplayName("댓글 작성 - 댓글 작성 시 게시글이 없는 경우")
    @Test
    public void post_comment_no_exist_article() {
        // given
        Long articleId = 1L;
        String userAccountId = "testId";
        String content = "comment test content";

        // when
        when(userAccountRepository.findByUserAccountId(userAccountId)).thenReturn(Optional.of(mock(UserAccount.class)));

        // then
        EotApplicationException e = assertThrows(EotApplicationException.class, () -> articleService.comment(articleId, userAccountId, content));
        Assertions.assertEquals(ErrorCode.POST_NOT_FOUND, e.getErrorCode());
    }
}