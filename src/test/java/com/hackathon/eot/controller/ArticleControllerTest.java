package com.hackathon.eot.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hackathon.eot.config.SecurityConfig;
import com.hackathon.eot.config.TestSecurityConfig;
import com.hackathon.eot.dto.request.PostCommentRequest;
import com.hackathon.eot.exception.EotApplicationException;
import com.hackathon.eot.exception.ErrorCode;
import com.hackathon.eot.model.constant.Gender;
import com.hackathon.eot.model.dto.ArticleDto;
import com.hackathon.eot.model.entity.ArticleEntity;
import com.hackathon.eot.model.entity.UserAccount;
import com.hackathon.eot.service.ArticleService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;

import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;

import java.io.FileInputStream;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("게시글 컨트롤러")
@Import({SecurityConfig.class, TestSecurityConfig.class})
@WebMvcTest(ArticleController.class)
@MockBean(JpaMetamodelMappingContext.class)
class ArticleControllerTest {

    private final MockMvc mvc;
    private final ObjectMapper objectMapper;

    @MockBean
    private ArticleService articleService;

    public ArticleControllerTest(@Autowired MockMvc mvc, @Autowired ObjectMapper objectMapper) {
        this.mvc = mvc;
        this.objectMapper = objectMapper;
    }

    @DisplayName("[POST] 게시글 컨트롤러 테스트 - 게시글 작성")
    @WithMockUser
    @Test
    public void article_post() throws Exception {
        // given & when
        String title = "test title";
        String content = "test content";

        String fileName = "testImage";
        String contentType = "jpeg";
        String filePath = "src/test/resources/testImage/" + fileName + "." + contentType;
        FileInputStream fileInputStream = new FileInputStream(filePath);

        MockMultipartFile file = new MockMultipartFile("images", fileName+"."+contentType, contentType, fileInputStream);

        // then
        mvc.perform(multipart("/api/articles")
                        .file(file)
                        .param("title", title)
                        .param("content", content)
                ).andDo(print())
                .andExpect(status().isOk());
    }

    @DisplayName("[POST] 게시글 컨트롤러 테스트 - 게시글 작성 시 로그인하지 않은 경우")
    @WithAnonymousUser
    @Test
    public void article_post_noLogin() throws Exception {
        // given & when
        String title = "test title";
        String content = "test content";

        String fileName = "testImage";
        String contentType = "jpeg";
        String filePath = "src/test/resources/testImage/" + fileName + "." + contentType;
        FileInputStream fileInputStream = new FileInputStream(filePath);

        MockMultipartFile file = new MockMultipartFile("images", fileName+"."+contentType, contentType, fileInputStream);

        // then
        mvc.perform(multipart("/api/articles")
                        .file(file)
                        .param("title", title)
                        .param("content", content)
                ).andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @DisplayName("[GET] 게시글 컨트롤러 테스트 - 전체 게시글 목록 조회")
    @WithMockUser
    @Test
    public void articles_get() throws Exception {
        // given & when
        when(articleService.articles(any())).thenReturn(Page.empty());

        // then
        mvc.perform(get("/api/articles")
                        .contentType(MediaType.APPLICATION_JSON)
                ).andDo(print())
                .andExpect(status().isOk());
    }

    @DisplayName("[GET] 게시글 컨트롤러 테스트 - 전체 게시글 목록 조회 시 로그인하지 않은 경우")
    @WithAnonymousUser
    @Test
    public void articles_get_noLogin() throws Exception {
        // given & when
        when(articleService.articles(any())).thenReturn(Page.empty());

        // then
        mvc.perform(get("/api/articles")
                        .contentType(MediaType.APPLICATION_JSON)
                ).andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @DisplayName("[GET] 게시글 컨트롤러 테스트 - 상세 게시글 조회")
    @WithMockUser
    @Test
    public void article_get() throws Exception {
        // given
        Long articleId = 1L;

        // when
        when(articleService.article(articleId)).thenReturn(ArticleDto.fromEntity(createArticle()));

        // then
        mvc.perform(get("/api/articles/1")
                        .contentType(MediaType.APPLICATION_JSON)
                ).andDo(print())
                .andExpect(status().isOk());
    }

    @DisplayName("[GET] 게시글 컨트롤러 테스트 - 상세 게시글 조회 시에 게시글이 없는 경우")
    @WithMockUser
    @Test
    public void article_get_no_exist_article() throws Exception {
        // given
        Long articleId = 1L;

        // when
        doThrow(new EotApplicationException(ErrorCode.POST_NOT_FOUND)).when(articleService).article(any());

        // then
        mvc.perform(get("/api/articles/1")
                        .contentType(MediaType.APPLICATION_JSON)
                ).andDo(print())
                .andExpect(status().isNotFound());
    }

    @DisplayName("[GET] 게시글 컨트롤러 테스트 - 댓글 조회")
    @WithMockUser
    @Test
    public void comments_get() throws Exception {
        // given & when
        when(articleService.comments(any(), any())).thenReturn(Page.empty());

        // then
        mvc.perform(get("/api/articles/1/comments")
                        .contentType(MediaType.APPLICATION_JSON)
                ).andDo(print())
                .andExpect(status().isOk());
    }

    @DisplayName("[GET] 게시글 컨트롤러 테스트 - 댓글 조회 시 게시글이 존재하지 않는 경우")
    @WithMockUser
    @Test
    public void comments_get_no_exists_article() throws Exception {
        // given & when
        doThrow(new EotApplicationException(ErrorCode.POST_NOT_FOUND)).when(articleService).comments(any(), any());

        // then
        mvc.perform(get("/api/articles/1/comments")
                        .contentType(MediaType.APPLICATION_JSON)
                ).andDo(print())
                .andExpect(status().isNotFound());
    }

    @DisplayName("[POST] 게시글 컨트롤러 테스트 - 댓글 작성")
    @WithMockUser
    @Test
    public void comment_post() throws Exception {
        mvc.perform(post("/api/articles/1/comments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(new PostCommentRequest("comment test content")))
                ).andDo(print())
                .andExpect(status().isOk());
    }

    @DisplayName("[POST] 게시글 컨트롤러 테스트 - 댓글 작성 시 로그인하지 않은 경우")
    @WithAnonymousUser
    @Test
    public void comment_post_noLogin() throws Exception {
        mvc.perform(post("/api/articles/1/comments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(new PostCommentRequest("comment test content")))
                ).andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @DisplayName("[POST] 게시글 컨트롤러 테스트 - 댓글 작성 시 게시글이 없는 경우")
    @WithMockUser
    @Test
    public void comment_post_no_exists_article() throws Exception {
        doThrow(new EotApplicationException(ErrorCode.POST_NOT_FOUND)).when(articleService).comment(any(), any(), any());

        mvc.perform(post("/api/articles/1/comments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(new PostCommentRequest("comment test content")))
                ).andDo(print())
                .andExpect(status().isNotFound());
    }

    private ArticleEntity createArticle() {
        ArticleEntity articleEntity = ArticleEntity.of(
                createUserAccount(),
                "title",
                "content"
        );
        ReflectionTestUtils.setField(articleEntity, "id", 1L);
        return articleEntity;
    }

    private UserAccount createUserAccount() {
        UserAccount user = UserAccount.of(
                "testId",
                "testPw",
                "name",
                "nick",
                "1990-12-01",
                Gender.MALE,
                "서울특별시 강동구");
        ReflectionTestUtils.setField(user, "id", 1L);
        return user;
    }
}