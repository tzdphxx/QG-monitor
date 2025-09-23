package com.qg.graph.service.impl;


import com.qg.common.domain.po.Result;

import com.qg.common.domain.vo.*;
import com.qg.feign.clients.BackendClient;
import com.qg.feign.clients.FrontendClient;
import com.qg.feign.clients.MobileClient;
import com.qg.graph.mapper.MethodInvocationMapper;
import com.qg.graph.service.GraphService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class GraphServiceImpl implements GraphService {

    private final FrontendClient frontendClient;
    private final BackendClient backendClient;
    private final MobileClient mobileClient;
    private final MethodInvocationMapper methodInvocationMapper;

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
     * 获取前端错误周报
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
     * 获取后端方法调用统计
     *
     * @param projectId 项目id
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return 结果
     */
    @Override
    public List<MethodInvocationVO> getMethodInvocationStats
    (String projectId, LocalDateTime startTime, LocalDateTime endTime) {
        return methodInvocationMapper.queryMethodInvocationStats(projectId, startTime, endTime);
    }

    /**
     * 查询指定时间段内所有IP的拦截次数统计
     *
     * @param projectId 项目ID
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return 拦截统计列表(IP和拦截次数)
     */
    @Override
    public List<IllegalAttackVO> getIpInterceptionCount
    (String projectId, LocalDateTime startTime, LocalDateTime endTime) {
        return backendClient.queryIpInterceptionCount(projectId, startTime, endTime);
    }

    /**
     * 查询指定时间段内所有境外访问的IP的拦截次数统计
     *
     * @param projectId 项目id
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return 结果
     */
    @Override
    public List<EarthVO> getForeignIpInterception
    (String projectId, LocalDateTime startTime, LocalDateTime endTime) {
        return backendClient.queryForeignIpInterceptions(projectId, startTime, endTime);
    }

    /**
     * 获取某个项目的访问量
     *
     * @param projectId 项目id
     * @param timeType  时间类型
     * @return 结果
     */
    @Override
    public Result getVisits(String projectId, String timeType) {
        return frontendClient.getVisits(projectId, timeType);
    }

    /**
     * 获取前端按钮数据
     *
     * @param projectId 项目id
     * @return 结果
     */
    @Override
    public Result getFrontendButton(String projectId) {
        return frontendClient.getFrontendButton(projectId);
    }

    /**
     * 获取前端api平均响应时间
     *
     * @param projectId 项目id
     * @param timeType  时间类型
     * @return 结果
     */
    @Override
    public Result getFrontApiAverageTime(String projectId, String timeType) {
        return frontendClient.getAverageTime(projectId, timeType);
    }

    /**
     * web端，获取后端错误统计
     *
     * @param projectId 项目id
     * @return 结果
     */
    @Override
    public Object[] getBackendErrorStats(String projectId) {
        return backendClient.getBackendErrorStats(projectId);
    }

    /**
     * app端，获取后端错误统计
     *
     * @param projectId 项目id
     * @return 结果
     */
    @Override
    public Object[] getBackendErrorStatsPro(String projectId) {
        return backendClient.getBackendErrorStatsPro(projectId);
    }

    /**
     * 获取后端api平均响应时间
     *
     * @param projectId 项目id
     * @param timeType  时间类型
     * @return 结果
     */
    @Override
    public Result getBackendApiAverageTime(String projectId, String timeType) {
        return frontendClient.getAverageTime(projectId, timeType);
    }

    /**
     * 获取移动端api平均响应时间
     *
     * @param projectId 项目id
     * @param timeType  时间类型
     * @return 结果
     */
    @Override
    public Result getMobileApiAverageTime(String projectId, String timeType) {
        return mobileClient.getAverageTime(projectId, timeType);
    }

    /**
     * 获取移动端操作性能
     *
     * @param projectId 项目id
     * @param timeType  时间类型
     * @return 结果
     */
    @Override
    public Result getMobileOperation(String projectId, String timeType) {
        return mobileClient.getMobileOperation(projectId, timeType);
    }

    /**
     * 网页端，获取移动端错误统计
     *
     * @param projectId 项目id
     * @return 结果
     */
    @Override
    public Object[] getMobileErrorStats(String projectId) {
        return mobileClient.getMobileErrorStats(projectId);
    }

    /**
     * app端，获取移动端错误统计
     *
     * @param projectId 项目id
     * @return 结果
     */
    @Override
    public Object[] getMobileErrorStatsPro(String projectId) {
        return mobileClient.getMobileErrorStatsPro(projectId);
    }
}
