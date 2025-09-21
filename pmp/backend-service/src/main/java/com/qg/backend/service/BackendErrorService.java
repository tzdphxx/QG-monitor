package com.qg.backend.service;


import com.qg.backend.domain.po.BackendError;
import com.qg.common.domain.po.Result;


/**
 * @Description: // 类说明
 * @ClassName: BackendErrorService    // 类名
 * @Author: lrt          // 创建者
 * @Date: 2025/8/7 21:30   // 时间
 * @Version: 1.0     // 版本
 */
public interface BackendErrorService {
    Result selectByCondition(String projectId, Long moduleId, String type);

    Integer saveBackendError(BackendError backendError);

    Result addBackendError(String errorData);

    Object[] getBackendErrorStats(String projectId);

    Object[] getBackendErrorStatsPro(String projectId);
}
