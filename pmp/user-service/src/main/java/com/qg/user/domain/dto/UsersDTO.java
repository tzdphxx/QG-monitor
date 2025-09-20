package com.qg.user.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UsersDTO {
    private Long id;
    private String username;
    private String avatar;
    private String phone;
    private String email;
    private LocalDateTime createdTime;
}
