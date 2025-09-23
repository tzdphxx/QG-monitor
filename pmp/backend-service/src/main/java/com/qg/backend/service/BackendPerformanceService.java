package com.qg.backend.service;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.qg.common.domain.po.BackendPerformance;
import com.qg.common.domain.po.Result;


import java.util.List;

/**
 * @Description: // 类说明
 * @ClassName: BackendPerformanceService    // 类名
 * @Author: lrt          // 创建者
 * @Date: 2025/8/7 21:31   // 时间
 * @Version: 1.0     // 版本
 */
public interface BackendPerformanceService {
    int saveBackendPerformance(List<BackendPerformance> backendPerformances);

    Result selectByCondition(String projectId, String api);

    Result addPerformance(String performanceData);

    Result getAverageTime(String projectId, String timeType);

    List<BackendPerformance> getBackendPerformanceByWrapper(LambdaQueryWrapper<BackendPerformance> queryWrapper);
}
