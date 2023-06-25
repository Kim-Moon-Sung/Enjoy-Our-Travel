package com.hackathon.eot.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hackathon.eot.config.SecurityConfig;
import com.hackathon.eot.config.TestSecurityConfig;
import com.hackathon.eot.service.ArticleService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;

import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.io.FileInputStream;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
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
}