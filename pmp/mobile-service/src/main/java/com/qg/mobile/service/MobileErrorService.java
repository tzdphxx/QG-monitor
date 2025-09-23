package com.qg.mobile.service;


import com.qg.common.domain.po.Result;

/**
 * @Description: // 类说明
 * @ClassName: MobileErrorService    // 类名
 * @Author: lrt          // 创建者
 * @Date: 2025/8/7 21:32   // 时间
 * @Version: 1.0     // 版本
 */
public interface MobileErrorService {
    Result selectByCondition(String projectId, String type);

    void receiveErrorFromSDK(String mobileErrorJSON);

    Object[] getMobileErrorStats(String projectId);

    Object[] getMobileErrorStatsPro(String projectId);
}
