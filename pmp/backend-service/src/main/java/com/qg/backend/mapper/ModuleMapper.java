package com.qg.backend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.qg.backend.domain.po.Module;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ModuleMapper extends BaseMapper<Module> {
}
