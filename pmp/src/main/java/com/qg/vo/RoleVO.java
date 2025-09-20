package com.qg.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class RoleVO {
    private Long userId;
    private String projectId;
    private Integer power;
    private Integer userRole;


    private Long currentUserId;
}
