package com.hackathon.eot.dto.response;

import com.hackathon.eot.model.constant.Gender;
import com.hackathon.eot.model.constant.UserRole;
import com.hackathon.eot.model.dto.User;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserJoinResponse {

    private Long id;
    private String userAccountId;
    private UserRole role;
    private String name;
    private String nickname;
    private String birthday;
    private Gender gender;
    private String address;

    public static UserJoinResponse fromUser(User user) {
        return new UserJoinResponse(user.getId(), user.getUsername(), user.getUserRole(),
                user.getName(), user.getNickname(), user.getBirthday(), user.getGender(), user.getAddress());
    }
}
