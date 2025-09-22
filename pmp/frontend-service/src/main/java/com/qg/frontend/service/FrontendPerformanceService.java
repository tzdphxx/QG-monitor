package com.qg.frontend.service;


import com.qg.common.domain.po.Result;
import com.qg.common.domain.vo.FrontendPerformanceAverageVO;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;

/**
 * @Description: // 类说明
 * @ClassName: FrontendPerformanceService    // 类名
 * @Author: lrt          // 创建者
 * @Date: 2025/8/7 21:32   // 时间
 * @Version: 1.0     // 版本
 */
public interface FrontendPerformanceService {

    Result saveFrontendPerformance(String data);

    Result selectByCondition(String projectId, String capture);

    Result getVisits(String projectId, String timeType);

    Result getAverageTime(String projectId, String timeType);

    FrontendPerformanceAverageVO queryAverageFrontendPerformanceTime(
            @Param("projectId") String projectId,
            @Param("startTime") LocalDateTime startTime,
            @Param("endTime") LocalDateTime endTime);
}
