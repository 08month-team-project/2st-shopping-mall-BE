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
    private String email;
    private String password;
    private String role;

    public static UserDetailsDTO toUserEntity(User user){
        return UserDetailsDTO.builder()
                .email(user.getEmail())
                .password(user.getPassword())
                .role(user.getRole().name()).build();
    }
}
