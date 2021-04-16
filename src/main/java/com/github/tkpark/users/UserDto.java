package com.github.tkpark.users;

import lombok.Data;

import java.time.LocalDateTime;

import static org.springframework.beans.BeanUtils.copyProperties;

@Data
public class UserDto {

    private String name;

    private String email;

    private int loginCount;

    private LocalDateTime lastLoginAt;

    private LocalDateTime createAt;

    public UserDto(User source) {
        copyProperties(source, this);
    }

}