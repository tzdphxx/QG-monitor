package com.qg.frontend.domain.vo;

import lombok.Data;

@Data
public class FrontendBehaviorVO {
    private String route;
    private Long avgTotalTime;
    private Long avgVisibleTime;
    private Integer samples;
}
