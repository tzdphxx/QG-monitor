package com.qg.alert.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.qg.alert.domain.po.Notification;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface NotificationMapper extends BaseMapper<Notification> {
}
