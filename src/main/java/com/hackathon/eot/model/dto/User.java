package com.hackathon.eot.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.hackathon.eot.model.constant.Gender;
import com.hackathon.eot.model.constant.UserRole;
import com.hackathon.eot.model.entity.UserAccount;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class User implements UserDetails {

    private Long id;
    private String username;
    private String password;
    private UserRole userRole;
    private String name;
    private String nickname;
    private String birthday;
    private Gender gender;
    private String address;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

    public static User fromEntity(UserAccount entity) {
        return new User(
                entity.getId(),
                entity.getUserAccountId(),
                entity.getPassword(),
                entity.getRole(),
                entity.getName(),
                entity.getNickname(),
                entity.getBirthday(),
                entity.getGender(),
                entity.getAddress(),
                entity.getCreatedAt(),
                entity.getModifiedAt()
        );
    }

    @Override
    @JsonIgnore
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(this.getUserRole().toString()));
    }

    @Override
    @JsonIgnore
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    @JsonIgnore
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    @JsonIgnore
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    @JsonIgnore
    public boolean isEnabled() {
        return true;
    }
}
