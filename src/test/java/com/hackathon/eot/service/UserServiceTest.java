package com.hackathon.eot.service;

import com.hackathon.eot.config.util.CustomUserService;
import com.hackathon.eot.dto.request.UserJoinRequest;
import com.hackathon.eot.exception.EotApplicationException;
import com.hackathon.eot.exception.ErrorCode;
import com.hackathon.eot.model.constant.Gender;
import com.hackathon.eot.model.dto.User;
import com.hackathon.eot.model.entity.UserAccount;
import com.hackathon.eot.repository.UserAccountRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;


@DisplayName("비즈니스 로직 - 유저 서비스")
@SpringBootTest
class UserServiceTest {

    @Autowired
    private UserService userService;

    @MockBean
    private CustomUserService customUserService;
    @MockBean
    private PasswordEncoder passwordEncoder;
    @MockBean
    private UserAccountRepository userAccountRepository;

    @DisplayName("회원가입이 정상적으로 동작하는 경우")
    @Test
    void join_success() {
        // given
        String userAccountId = "testId";
        String password = "testPw";

        UserJoinRequest request = new UserJoinRequest(userAccountId, password, "name", "nickname", "2001-01-12", Gender.MALE, "서울특별시 강남구");

        // when
        when(userAccountRepository.findByUserAccountId(userAccountId)).thenReturn(Optional.empty());
        when(passwordEncoder.encode(password)).thenReturn("encrypt_pw");
        when(userAccountRepository.save(any())).thenReturn(getUser(userAccountId, password, 1L));

        // then
        Assertions.assertDoesNotThrow(() -> userService.join(request));
    }

    @DisplayName("회원가입 시 중복된 유저 계정 아디이가 있는 경우 에러 반환")
    @Test
    public void join_error() {
        // given
        String userAccountId = "testId";
        String password = "testPw";

        UserJoinRequest request = new UserJoinRequest(userAccountId, password, "name", "nickname", "2001-01-12", Gender.MALE, "서울특별시 강남구");
        UserAccount user = getUser(userAccountId, password, 1L);

        // when
        when(userAccountRepository.findByUserAccountId(userAccountId)).thenReturn(Optional.of(user));

        // then
        EotApplicationException e = Assertions.assertThrows(EotApplicationException.class, () -> userService.join(request));
        Assertions.assertEquals(ErrorCode.DUPLICATED_USER_ACCOUNT_ID, e.getErrorCode());
    }

    @DisplayName("유저 로그인이 정상적으로 작동하는 경우")
    @Test
    public void login_success() {
        // given
        String accountId = "testId";
        String password = "testPw";

        UserAccount user = getUser(accountId, password, 1L);

        // when
        when(customUserService.loadUserByUsername(accountId)).thenReturn(User.fromEntity(user));
        when(passwordEncoder.matches(password, user.getPassword())).thenReturn(true);

        // then
        Assertions.assertDoesNotThrow(() -> userService.login(accountId, password));
    }

    @DisplayName("로그인 시에 가입된 회원이 아닌 경우")
    @Test
    public void login_no_exist_account_id() {
        // given
        String userAccountId = "testId";
        String password = "password";

        // when
        when(userAccountRepository.findByUserAccountId(userAccountId)).thenReturn(Optional.empty());
        when(customUserService.loadUserByUsername(userAccountId)).thenThrow(new EotApplicationException(ErrorCode.USER_NOT_FOUND));

        // then
        EotApplicationException e = Assertions.assertThrows(EotApplicationException.class,
                () -> userService.login(userAccountId, password));
        Assertions.assertEquals(ErrorCode.USER_NOT_FOUND, e.getErrorCode());
    }

    @DisplayName("로그인시 패스워드가 틀린 경우")
    @Test
    public void login_wrong_password() {
        // given
        String userAccountId = "testId";
        String password = "testPw";
        String wrongPw = "wrongPw";

        UserAccount user = getUser(userAccountId, password, 1L);

        // when
        when(customUserService.loadUserByUsername(userAccountId)).thenReturn(User.fromEntity(user));
        when(userAccountRepository.findByUserAccountId(userAccountId)).thenReturn(Optional.of(user));

        // then
        EotApplicationException e = Assertions.assertThrows(EotApplicationException.class, () ->
                userService.login(userAccountId, wrongPw));
        Assertions.assertEquals(ErrorCode.INVALID_PASSWORD, e.getErrorCode());
    }

    private UserAccount getUser(String userAccountId, String password, Long id) {
        return UserAccount.getTestUser(id, userAccountId, password, "name", "nickname", "2001-01-12", Gender.MALE, "서울특별시 강남구");
    }
}