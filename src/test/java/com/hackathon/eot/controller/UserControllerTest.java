package com.hackathon.eot.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hackathon.eot.config.SecurityConfig;
import com.hackathon.eot.config.TestSecurityConfig;
import com.hackathon.eot.dto.request.UserJoinRequest;
import com.hackathon.eot.model.constant.Gender;
import com.hackathon.eot.model.dto.User;
import com.hackathon.eot.service.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("유저 컨트롤러")
@Import({SecurityConfig.class, TestSecurityConfig.class})
@WebMvcTest(UserController.class)
@MockBean(JpaMetamodelMappingContext.class)
class UserControllerTest {

    private final MockMvc mvc;
    private final ObjectMapper objectMapper;

    @MockBean
    private UserService userService;

    public UserControllerTest(@Autowired MockMvc mvc, @Autowired ObjectMapper objectMapper) {
        this.mvc = mvc;
        this.objectMapper = objectMapper;
    }

    @DisplayName("[POST] 유저 컨트롤러 테스트 - 회원가입")
    @Test
    public void join() throws Exception {
        // given
        UserJoinRequest request = new UserJoinRequest("testId", "testPw", "testName",
                "nick", "2002-03-11", Gender.MALE, "서울특별시 시험동");

        // when
        when(userService.join(any(UserJoinRequest.class))).thenReturn(mock(User.class));

        mvc.perform(post("/api/users/join")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                ).andDo(print())
                .andExpect(status().isOk());

        // then
    }
}