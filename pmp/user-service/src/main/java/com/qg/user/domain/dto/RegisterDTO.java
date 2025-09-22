package com.qg.user.domain.dto;


import com.qg.common.domain.po.Users;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterDTO {
    private Users users;
    private String code;

}
