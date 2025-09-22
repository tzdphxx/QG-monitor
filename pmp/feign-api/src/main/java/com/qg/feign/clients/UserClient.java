package com.qg.feign.clients;


import com.qg.common.domain.po.Result;
import com.qg.feign.dto.UsersDto;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.web.bind.annotation.RequestParam;

import java.util.Collection;
import java.util.List;

@FeignClient(value = "user-service")
public interface UserClient {

    @GetMapping("/users/findUserById")
    UsersDto findUserById(@RequestParam("id") Long id);

    @GetMapping("/users/findUserByIds")
    List<UsersDto> findUserByIds(@RequestParam("ids") Collection<Long> ids);
}
