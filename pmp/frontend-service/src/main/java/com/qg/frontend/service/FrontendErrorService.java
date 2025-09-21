package com.qg.frontend.service;



import com.qg.common.domain.po.Result;
import com.qg.common.domain.po.FrontendError;

import java.util.List;

/**
 * @Description: // 类说明
 * @ClassName: FrontendErrorService    // 类名
 * @Author: lrt          // 创建者
 * @Date: 2025/8/7 21:31   // 时间
 * @Version: 1.0     // 版本
 */
public interface FrontendErrorService {
    Result selectByCondition(String projectId, String type);

    Integer saveFrontendError(List<FrontendError> frontendErrors);

    Result addFrontendError(String errorData);


    Object[] getErrorStats(String projectId);

    Result getAverageTime(String projectId, String timeType);
}
