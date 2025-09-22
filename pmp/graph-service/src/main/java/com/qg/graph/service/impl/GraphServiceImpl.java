package com.qg.graph.service.impl;


import com.qg.common.domain.vo.*;
import com.qg.feign.clients.FrontendClient;
import com.qg.graph.domain.vo.*;
import com.qg.graph.service.GraphService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
public class GraphServiceImpl implements GraphService {

    @Autowired
    private FrontendClient frontendClient;

    /**
     * 查询指定时间段内某项目中，用户页面停留《所有路由下》时间数据
     *
     * @param projectId 项目id
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return 查询结果
     */
    @Override
    public List<FrontendBehaviorVO> queryTimeDataByProjectIdAndTimeRange
    (String projectId, LocalDateTime startTime, LocalDateTime endTime) {
        return frontendClient.queryTimeDataByProjectIdAndTimeRange(projectId, startTime, endTime);
    }


    /**
     * 查询指定时间段内某项目中，用户页面停留《某路由下》时间数据
     *
     * @param projectId 项目id
     * @param route     查询的路由
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return 结果
     */
    @Override
    public List<FrontendBehaviorVO> queryTimeDataByProjectIdAndTimeRangeAndRoute
    (String projectId, String route, LocalDateTime startTime, LocalDateTime endTime) {
        return frontendClient.queryTimeDataByProjectIdAndTimeRangeAndRoute(projectId, route, startTime, endTime);
    }

    /**
     * 按时间（允许按照时间筛选）以及错误类别（前端/后端/移动）展示错误量
     *
     * @param projectId 项目id
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return 结果
     */
    @Override
    public List<ErrorTrendVO> getErrorTrend
    (String projectId, LocalDateTime startTime, LocalDateTime endTime) {
        return frontendClient.getErrorTrend(projectId, startTime, endTime);
    }

    /**
     * 获取埋点错误统计
     *
     * @param projectId 项目id
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return 结果
     */
    @Override
    public List<ManualTrackingVO> getManualTrackingStats
    (String projectId, LocalDateTime startTime, LocalDateTime endTime) {
        return frontendClient.queryManualTrackingStats(projectId, startTime, endTime);
    }

    /**
     * 获取两种前端错误信息
     *
     * @param projectId 项目id
     * @return 结果
     */
    @Override
    public Object[] getErrorStats(String projectId) {
        return frontendClient.getErrorStats(projectId);
    }

    /**
     * 获取前端性能，加载时间平均数据
     *
     * @param projectId 项目id
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return 结果
     */
    @Override
    public FrontendPerformanceAverageVO getAverageFrontendPerformanceTime
    (String projectId, LocalDateTime startTime, LocalDateTime endTime) {
        return frontendClient.queryAverageFrontendPerformanceTime(projectId, startTime, endTime);
    }


    /**
     * 获取方法调用统计
     *
     * @param projectId
     * @param startTime
     * @param endTime
     * @return
     */
    @Override
    public List<MethodInvocationVO> getMethodInvocationStats
    (String projectId, LocalDateTime startTime, LocalDateTime endTime) {
        return methodInvocationMapper.queryMethodInvocationStats(projectId, startTime, endTime);
    }

    /**
     * 获取非法攻击统计
     *
     * @param projectId
     * @param startTime
     * @param endTime
     * @return
     */
    @Override
    public List<IllegalAttackVO> getIpInterceptionCount
    (String projectId, LocalDateTime startTime, LocalDateTime endTime) {
        return backendLogMapper.queryIpInterceptionCount(projectId, startTime, endTime);
    }

    /**
     * 获取非法攻击外网IP数据
     *
     * @param projectId
     * @param startTime
     * @param endTime
     * @return
     */
    @Override
    public List<EarthVO> getForeignIpInterception
    (String projectId, LocalDateTime startTime, LocalDateTime endTime) {
        return backendLogMapper.queryForeignIpInterceptions(projectId, startTime, endTime);
    }
}
