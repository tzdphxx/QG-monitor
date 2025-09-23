package com.qg.feign.clients;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.qg.common.domain.po.MobileError;
import com.qg.common.domain.po.MobilePerformance;
import com.qg.common.domain.po.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;


@FeignClient("mobile-service")
@RequestMapping("/mobile")
public interface MobileClient {

    /**
     * 获取移动端api平均响应时间
     *
     * @param projectId 项目id
     * @param timeType  时间类型
     * @return 结果
     */
    @GetMapping("/getAverageTime")
    Result getAverageTime(@RequestParam String projectId, @RequestParam String timeType);

    /**
     * 获取移动端操作性能
     *
     * @param projectId 项目id
     * @param timeType  时间类型
     * @return 结果
     */
    @GetMapping("/getMobileOperation")
    Result getMobileOperation(@RequestParam String projectId, @RequestParam String timeType);

    /**
     * 网页端，获取移动端错误统计
     *
     * @param projectId 项目id
     * @return 结果
     */
    @GetMapping("/getMobileErrorStats")
    Object[] getMobileErrorStats(@RequestParam String projectId);

    /**
     * app端，获取移动端错误统计
     *
     * @param projectId 项目id
     * @return 结果
     */
    @GetMapping("/getMobileErrorStatsPro")
    Object[] getMobileErrorStatsPro(@RequestParam String projectId);

    @GetMapping("/getMobileErrorByWrapper")
    List<MobileError> getMobileErrorByWrapper(@RequestParam LambdaQueryWrapper<MobileError> wrapper);

    @GetMapping("/getMobilePerformanceByWrapper")
    List<MobilePerformance> getMobilePerformanceByWrapper(@RequestParam LambdaQueryWrapper<MobilePerformance> wrapper);
}
