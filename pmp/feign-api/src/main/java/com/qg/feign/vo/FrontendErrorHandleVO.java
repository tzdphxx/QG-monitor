package com.qg.feign.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.qg.common.handler.ListHandler;
import com.qg.common.handler.MapHandler;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Data
public class FrontendErrorHandleVO {
    private Long id;
    private String projectId;
    private LocalDateTime timestamp;
    private String sessionId;
    private String userAgent;
    private String errorType;
    private String message;
    private String stack;
    private String network;

    private Integer event;

    @TableField(typeHandler = MapHandler.class, value = "request_info")
    private Map<String, Object> request;
    @TableField(typeHandler = MapHandler.class, value = "response_info")
    private Map<String, Object> response;
    @TableField(typeHandler = MapHandler.class, value = "resource_info")
    private Map<String, Object> resource;
    @TableField(typeHandler = ListHandler.class)
    private List<Map<String, Object>> breadcrumbs;
    @TableField(typeHandler = MapHandler.class)
    private Map<String, Object> tags;
    @TableField(typeHandler = MapHandler.class)
    private Map<String, Object> elementInfo;
    private String captureType;
    private Long duration;

    private Integer isHandle;
}
