package com.qg.feign.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @Description: // 类说明
 * @ClassName: ResponsibilityClient    // 类名
 * @Author: lrt          // 创建者
 * @Date: 2025/9/22 11:54   // 时间
 * @Version: 1.0     // 版本
 */
@FeignClient("alert-service")
public interface AlertClient {
    @DeleteMapping("/responsibilities/deleteUserId")
    int deleteByUserId(@RequestParam Long userId);
}
