package com.hackathon.eot.model.entity;

import com.hackathon.eot.model.constant.Gender;
import com.hackathon.eot.model.constant.UserRole;
import lombok.*;

import javax.persistence.*;

@Getter
@Table(name = "user")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class UserAccount extends AuditingFields {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_account_id", nullable = false)
    private String userAccountId;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "role")
    @Enumerated(EnumType.STRING)
    private UserRole role = UserRole.USER;

    @Column(name = "name")
    private String name;

    @Column(name = "nickname")
    private String nickname;

    @Column(name = "birthday")
    private String birthday;

    @Enumerated(EnumType.STRING)
    @Column(name = "gender")
    private Gender gender;

    @Column(name = "address")
    private String address;

    private UserAccount(String userAccountId, String password, String name, String nickname, String birthday, Gender gender, String address) {
        this.userAccountId = userAccountId;
        this.password = password;
        this.name = name;
        this.nickname = nickname;
        this.birthday = birthday;
        this.gender = gender;
        this.address = address;
    }

    public static UserAccount of(String userAccountId, String password, String name, String nickname, String birthday, Gender gender, String address) {
        return new UserAccount(userAccountId, password, name, nickname, birthday, gender, address);
    }
}
