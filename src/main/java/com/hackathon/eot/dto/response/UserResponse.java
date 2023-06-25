package com.hackathon.eot.dto.response;

import com.hackathon.eot.model.constant.Gender;
import com.hackathon.eot.model.dto.User;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserResponse {

    private Long id;
    private String userAccountId;
    private String name;
    private String nickname;
    private String birthday;
    private Gender gender;
    private String address;

    public static UserResponse fromUser(User user) {
        return new UserResponse(
                user.getId(),
                user.getUsername(),
                user.getName(),
                user.getNickname(),
                user.getBirthday(),
                user.getGender(),
                user.getAddress()
        );
    }
}
