package com.qg.common.domain.dto;

import lombok.Data;

@Data
public class ErrorStatsDTO {
    private String errorType;
    private Integer count;
    private Double ratio;
}
