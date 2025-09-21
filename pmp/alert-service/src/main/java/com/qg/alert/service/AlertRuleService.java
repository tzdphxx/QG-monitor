package com.qg.alert.service;


import com.qg.alert.domain.po.AlertRule;
import com.qg.common.domain.po.Result;

public interface AlertRuleService {
    Result selectByType(String errorType, String env, String projectId, String platform);

    Result updateThreshold(AlertRule alertRule);
}
