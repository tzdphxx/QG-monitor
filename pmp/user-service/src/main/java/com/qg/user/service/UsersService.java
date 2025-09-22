package com.qg.user.service;


import com.qg.common.domain.po.Result;
import com.qg.user.domain.dto.UsersDTO;
import com.qg.user.domain.po.Users;
import java.util.Map;

public interface UsersService {
    Map<String,Object> loginByPassword(String email, String password);

    Result register(Users user, String code);

    Result sendCodeByEmail(String email);

    Result getUser(Long id);

    Result findPassword(Users user, String code);

    boolean updateAvatar(Long userId, String avatarUrl);

    Result updateUser(Users users);

    UsersDTO findUserById(Long id);
}
