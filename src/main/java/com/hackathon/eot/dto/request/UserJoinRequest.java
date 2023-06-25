package com.hackathon.eot.dto.request;

import com.hackathon.eot.model.constant.Gender;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserJoinRequest {

    private String userAccountId;
    private String password;
    private String name;
    private String nickname;
    private String birthday;
    private Gender gender;
    private String address;
}
