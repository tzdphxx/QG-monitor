package com.qg.feign.clients;


import com.qg.feign.dto.UsersDto;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "user-service")
public interface UserClient {

    @GetMapping("/users/findUserById")
    UsersDto findUserById(@RequestParam("id") Long id);
}
