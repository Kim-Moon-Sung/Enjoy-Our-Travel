package com.hackathon.eot.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hackathon.eot.config.SecurityConfig;
import com.hackathon.eot.config.TestSecurityConfig;
import com.hackathon.eot.dto.request.UserJoinRequest;
import com.hackathon.eot.dto.request.UserLoginRequest;
import com.hackathon.eot.exception.EotApplicationException;
import com.hackathon.eot.exception.ErrorCode;
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
import static org.mockito.ArgumentMatchers.anyString;
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
    public void join_success() throws Exception {
        // given & when
        UserJoinRequest request = new UserJoinRequest("testId", "testPw", "testName",
                "nick", "2002-03-11", Gender.MALE, "서울특별시 시험동");

        // then
        when(userService.join(any(UserJoinRequest.class))).thenReturn(mock(User.class));

        mvc.perform(post("/api/users/join")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(request))
                ).andDo(print())
                .andExpect(status().isOk());
    }

    @DisplayName("[POST] 유저 컨트롤러 테스트 - 회원가입, 중복된 아이디가 있는 경우 에러 반환")
    @Test
    public void join_error() throws Exception {
        // given & when
        UserJoinRequest request = new UserJoinRequest("testId", "testPw", "testName",
                "nick", "2002-03-11", Gender.MALE, "서울특별시 시험동");

        when(userService.join(any(UserJoinRequest.class))).thenThrow(new EotApplicationException(ErrorCode.DUPLICATED_USER_ACCOUNT_ID));

        // then
        mvc.perform(post("/api/users/join")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(request))
                ).andDo(print())
                .andExpect(status().isConflict());
    }

    @DisplayName("[POST] 유저 컨트롤러 테스트 - 로그인")
    @Test
    public void login_success() throws Exception {
        // given & when
        String userAccountId = "testId";
        String password = "testPw";

        when(userService.login(userAccountId, password)).thenReturn("test_jwt_token");

        // then
        mvc.perform(post("/api/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(new UserLoginRequest(userAccountId, password)))
                ).andDo(print())
                .andExpect(status().isOk());

    }

    @DisplayName("[POST] 유저 컨트롤러 테스트 - 로그인시 존재하지 않는 계정 ID를 입력할 경우 에러 반환")
    @Test
    public void login_no_exist_accountId() throws Exception {
        // given
        String userAccountId = "noExistId";
        String password = "testPw";

        when(userService.login(userAccountId, password)).thenThrow(new EotApplicationException(ErrorCode.USER_NOT_FOUND));

        // then
        mvc.perform(post("/api/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(new UserLoginRequest(userAccountId, password)))
                ).andDo(print())
                .andExpect(status().isNotFound());
    }

    @DisplayName("[POST] 유저 컨트롤러 테스트 - 로그인 시 잘못된 패스워드 입력 시에 에러 반환")
    @Test
    public void login_wrong_password() throws Exception {
        // given & when
        String userAccountId = "testId";
        String password = "password";

        when(userService.login(userAccountId, password)).thenThrow(new EotApplicationException(ErrorCode.INVALID_PASSWORD));

        // then
        mvc.perform(post("/api/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(new UserLoginRequest(userAccountId, password)))
                ).andDo(print())
                .andExpect(status().isUnauthorized());
    }
}