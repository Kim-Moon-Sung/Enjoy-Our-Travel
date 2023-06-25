package com.hackathon.eot.config;

import com.hackathon.eot.config.util.CustomUserService;
import com.hackathon.eot.model.constant.Gender;
import com.hackathon.eot.model.entity.UserAccount;
import com.hackathon.eot.repository.UserAccountRepository;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.event.annotation.BeforeTestMethod;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

@Import(SecurityConfig.class)
public class TestSecurityConfig {

    @MockBean
    private CustomUserService customUserService;
    @MockBean private UserAccountRepository userAccountRepository;

    @BeforeTestMethod
    public void setup() {
        given(userAccountRepository.findByUserAccountId(anyString())).willReturn(
                Optional.of(UserAccount.of("testAccountId", "testPw", "testName", "nick",
                        "2002-12-21", Gender.MALE, "서울특별시 시험동"))
        );
    }
}
