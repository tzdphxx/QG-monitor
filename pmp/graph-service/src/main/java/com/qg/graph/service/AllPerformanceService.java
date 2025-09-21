package com.qg.graph.service;


import com.qg.common.domain.po.Result;

/**
 * @Description: // 类说明
 * @ClassName: AllPerformanceService    // 类名
 * @Author: lrt          // 创建者
 * @Date: 2025/8/9 11:28   // 时间
 * @Version: 1.0     // 版本
 */
public interface AllPerformanceService {
    Result selectByCondition(String projectId, String api, String capture, String osVersion);
}
