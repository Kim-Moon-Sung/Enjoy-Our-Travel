package com.hackathon.eot.service;

import com.hackathon.eot.config.util.CustomUserService;
import com.hackathon.eot.config.util.JwtTokenUtils;
import com.hackathon.eot.dto.request.UserJoinRequest;
import com.hackathon.eot.exception.EotApplicationException;
import com.hackathon.eot.exception.ErrorCode;
import com.hackathon.eot.model.dto.User;
import com.hackathon.eot.model.entity.UserAccount;
import com.hackathon.eot.repository.UserAccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final CustomUserService customUserService;
    private final PasswordEncoder passwordEncoder;
    private final UserAccountRepository userAccountRepository;

    @Value("${jwt.secret-key}")
    private String secretKey;

    @Value("${jwt.token.expired-time-ms}")
    private Long expiredTimeMs;

    @Transactional
    public User join(UserJoinRequest request) {
        userAccountRepository.findByUserAccountId(request.getUserAccountId()).ifPresent(it -> {
            throw new EotApplicationException(ErrorCode.DUPLICATED_USER_ACCOUNT_ID, String.format("%s is duplicated", request.getUserAccountId()));
        });

        UserAccount user = userAccountRepository.save(UserAccount.of(request.getUserAccountId(), passwordEncoder.encode(request.getPassword()),
                request.getName(), request.getNickname(), request.getBirthday(), request.getGender(), request.getAddress()));
        return User.fromEntity(user);
    }

    public String login(String userAccountId, String password) {
        // 회원가입 여부 체크
        User user = customUserService.loadUserByUsername(userAccountId);

        // 비밀번호 체크
        if(!passwordEncoder.matches(password, user.getPassword())) {
            throw new EotApplicationException(ErrorCode.INVALID_PASSWORD);
        }

        // jwt 토큰 생성
        return JwtTokenUtils.generateToken(userAccountId, secretKey, expiredTimeMs);
    }
}
