package com.qg.project.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProjectMemberVO {
    private Long id;
    private Long userId;
    private String username;
    private String avatar;
    private Integer userRole;
    private Integer power;

}
