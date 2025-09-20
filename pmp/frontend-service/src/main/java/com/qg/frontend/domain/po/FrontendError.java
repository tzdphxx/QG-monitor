package com.qg.frontend.domain.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.qg.common.handler.MapHandler;
import com.qg.common.handler.ListHandler;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * @Description: 前端错误类  // 类说明
 * @ClassName: frontendError    // 类名
 * @Author: lrt          // 创建者
 * @Date: 2025/8/7 21:09   // 时间
 * @Version: 1.0     // 版本
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FrontendError {
    @TableId(value = "id",type = IdType.AUTO)
    private Long id;
    private String projectId;
    private LocalDateTime timestamp;
    private String sessionId;
    private String userAgent;
    private String errorType;
    private String message;
    private String stack;
    private String network;

    private Integer event = 1;

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

    private Integer colno;
    private Integer lineno;
    private String jsFilename;


    // 原子性递增
    public synchronized void incrementEvent() {
        event++;
    }

    // 获取当前值
    public synchronized Integer getEvent() {
        return event;
    }
}
