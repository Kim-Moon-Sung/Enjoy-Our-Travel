package com.hackathon.eot.service;

import com.hackathon.eot.config.util.CustomUserService;
import com.hackathon.eot.dto.request.UserJoinRequest;
import com.hackathon.eot.exception.EotApplicationException;
import com.hackathon.eot.exception.ErrorCode;
import com.hackathon.eot.model.constant.Gender;
import com.hackathon.eot.model.entity.UserAccount;
import com.hackathon.eot.repository.UserAccountRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.refEq;
import static org.mockito.Mockito.when;


@DisplayName("비즈니스 로직 - 유저 서비스")
@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock private CustomUserService customUserService;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private UserAccountRepository userAccountRepository;

    @DisplayName("회원가입이 정상적으로 동작하는 경우")
    @Test
    void login_success() {
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

    private UserAccount getUser(String userAccountId, String password, Long id) {
        return UserAccount.of(userAccountId, password, "name", "nickname", "2001-01-12", Gender.MALE, "서울특별시 강남구");
    }
}