package com.qg.backend.service;


import com.qg.backend.domain.po.BackendLog;
import com.qg.common.domain.vo.EarthVO;
import com.qg.common.domain.vo.IllegalAttackVO;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @Description: // 类说明
 * @ClassName: BackendLogService    // 类名
 * @Author: lrt          // 创建者
 * @Date: 2025/8/7 21:30   // 时间
 * @Version: 1.0     // 版本
 */
public interface BackendLogService {

    List<BackendLog> getAllLogs(String projectId);

    List<BackendLog> getLogsByCondition(String evn, String logLevel, String projectId);

    void receiveLogFromSDK(String logJSON);

    List<IllegalAttackVO> queryIpInterceptionCount(
            String projectId, LocalDateTime startTime, LocalDateTime endTime);

    List<EarthVO> queryForeignIpInterceptions(
            String projectId,
            LocalDateTime startTime,
            LocalDateTime endTime);
}
