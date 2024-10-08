package com.example.shoppingmall.global.security.dto;

import com.example.shoppingmall.domain.user.domain.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDetailsDTO {

    private Long userId;
    private String name;
    private String nickname;
    private String email;
    private String password;
    private String phoneNumber;
    private String gender;
    private String role;

    public static UserDetailsDTO from(User user){
        return UserDetailsDTO.builder()
                .userId(user.getId())
                .name(user.getName())
                .nickname(user.getNickname())
                .email(user.getEmail())
                .password(user.getPassword())
                .gender(user.getGender().name())
                .phoneNumber(user.getPhoneNumber())
                .role(user.getRole().name()).build();
    }
}
