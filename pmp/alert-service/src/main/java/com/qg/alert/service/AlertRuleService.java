package com.qg.alert.service;


import com.qg.alert.domain.po.AlertRule;
import com.qg.common.domain.po.Result;

import java.util.HashMap;

public interface AlertRuleService {
    Result selectByType(String errorType, String env, String projectId, String platform);

    Result updateThreshold(AlertRule alertRule);

    Integer selectThresholdByProjectAndErrorType(String projectId, String errorType, String platform);

    HashMap<String, Integer> selectByBackendRedisKeyToMap(String projectId, String errorType, String environment);
}
