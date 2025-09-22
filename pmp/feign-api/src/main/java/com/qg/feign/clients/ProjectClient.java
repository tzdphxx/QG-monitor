package com.qg.feign.clients;


import com.qg.common.domain.po.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @Description: // 类说明
 * @ClassName: ProjectClient    // 类名
 * @Author: lrt          // 创建者
 * @Date: 2025/9/22 10:53   // 时间
 * @Version: 1.0     // 版本
 */
@FeignClient("project-service")
public interface ProjectClient {

    @GetMapping("/projects/getPort")
    Result getProjectList(@RequestParam String uuid);
}
