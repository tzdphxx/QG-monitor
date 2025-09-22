package com.qg.project.service;


import com.qg.common.domain.po.Result;
import com.qg.project.domain.po.Role;
import com.qg.project.domain.vo.RoleVO;


public interface RoleService {
    Result addRole(Role role);

    Result updateRole(RoleVO roleVO);

    Result deleteRole(String projectId, Long userId);

    Result getMemberList(String projectId);

    Result getRole(Long userId, String projectId);

    Result updateUserRole(Role role);

    Result getBossCountByProjectId(String projectId);
}
