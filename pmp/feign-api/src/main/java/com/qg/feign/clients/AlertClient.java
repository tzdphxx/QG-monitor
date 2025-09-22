package com.qg.feign.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

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


    @GetMapping("/alertRules/selectThresholdByProjectAndErrorType")
    Integer selectThresholdByProjectAndErrorType(@RequestParam String projectId,
                                                 @RequestParam String errorType, @RequestParam String platform);

    @GetMapping("/responsibilities/getResponsibilityByQueryWrapper")
    Responsibility getResponsibilityByQueryWrapper(@RequestParam LambdaQueryWrapper<Responsibility> queryWrapper);


    @PutMapping("/responsibilities/updateResponsibilityByWrapper")
    Integer updateResponsibilityByWrapper(@RequestParam Responsibility responsibility, @RequestParam LambdaQueryWrapper<Responsibility> queryWrapper);

    @GetMapping("/responsibilities/getResponsibilityListByWrapper")
    List<Responsibility> getResponsibilityListByWrapper(@RequestParam LambdaQueryWrapper<Responsibility> queryWrapper);

    @GetMapping("/notifications/getNotificationByWrapper")
    Notification getNotificationByWrapper(@RequestParam LambdaQueryWrapper<Notification> queryWrapper);

    @GetMapping("/alertRules/selectByBackendRedisKeyToMap")
    HashMap<String, Integer> selectByBackendRedisKeyToMap(@RequestParam String projectId,
                                                          @RequestParam String errorType, @RequestParam String environment);

    @PostMapping("/notifications/addNotifications")
    int addNotification(@RequestBody List<Notification> notifications);

}
