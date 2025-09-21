package com.qg.backend.service;


import com.qg.common.domain.po.Result;

public interface ModuleService {
    Result addModule(Module module);

    Result selectByProjectId(String projectId);

    Result deleteById(Long id);

    void putModuleIfAbsent(String moduleName, String projectId);
}
