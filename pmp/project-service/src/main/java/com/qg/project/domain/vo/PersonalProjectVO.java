package com.qg.project.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PersonalProjectVO {
    private String uuid;
    private String name;
    private String description;
    private LocalDateTime createdTime;
    private Boolean isPublic;

//    private Long id;
    private Long userId;

    private Integer power;
    private Integer userRole;

}
