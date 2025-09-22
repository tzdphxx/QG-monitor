package com.qg.feign.clients;


import com.qg.common.domain.po.Result;
import com.qg.feign.dto.UsersDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "user-service")
@RequestMapping("/users")
public interface UserClient {

    @GetMapping("/findUserById")
    UsersDto findUserById(@RequestParam("id") Long id);

    @GetMapping("/{id}")
    Result getUser(@PathVariable Long id);
}
