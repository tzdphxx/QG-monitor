package com.qg.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @Description: 移动错误Vo  // 类说明
 * @ClassName: MobileResponsibilityVO    // 类名
 * @Author: lrt          // 创建者
 * @Date: 2025/8/9 15:21   // 时间
 * @Version: 1.0     // 版本
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MobileResponsibilityVO {

    private Long id;
    private String projectId;
    private LocalDateTime timestamp;
    private String errorType;
    private String message;
    private String stack;
    private String className;
    private Integer event;


    private String name;
    private Long delegatorId;
    private Long responsibleId;
    private String avatarUrl;
}
