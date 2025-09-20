package com.qg.service;

import com.qg.domain.Result;
import com.qg.domain.Role;
import com.qg.vo.RoleVO;

public interface RoleService {
    Result addRole(Role role);

    Result updateRole(RoleVO roleVO);

    Result deleteRole(String projectId, Long userId);

    Result getMemberList(String projectId);

    Result getRole(Long userId, String projectId);

    Result updateUserRole(Role role);

    Result getBossCountByProjectId(String projectId);
}
