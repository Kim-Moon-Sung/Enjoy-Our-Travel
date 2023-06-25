package com.hackathon.eot.service;

import com.hackathon.eot.dto.request.UserJoinRequest;
import com.hackathon.eot.exception.EotApplicationException;
import com.hackathon.eot.exception.ErrorCode;
import com.hackathon.eot.model.dto.User;
import com.hackathon.eot.model.entity.UserAccount;
import com.hackathon.eot.repository.UserAccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserAccountRepository userAccountRepository;

    @Transactional
    public User join(UserJoinRequest request) {
        userAccountRepository.findByUserAccountId(request.getUserAccountId()).ifPresent(it -> {
            throw new EotApplicationException(ErrorCode.DUPLICATED_USER_ACCOUNT_ID, String.format("%s is duplicated", request.getUserAccountId()));
        });

        UserAccount user = userAccountRepository.save(UserAccount.of(request.getUserAccountId(), request.getPassword(),
                request.getName(), request.getNickname(), request.getBirthday(), request.getGender(), request.getAddress()));
        return User.fromEntity(user);
    }
}
