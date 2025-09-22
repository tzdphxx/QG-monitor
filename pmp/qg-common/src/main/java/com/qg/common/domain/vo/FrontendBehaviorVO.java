package com.qg.common.domain.vo;

import lombok.Data;

@Data
public class FrontendBehaviorVO {
    private String route;
    private Long avgTotalTime;
    private Long avgVisibleTime;
    private Integer samples;
}
