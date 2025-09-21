package com.qg.common.utils;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import lombok.NonNull;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

@Component
public class SQLUtil implements ApplicationContextAware {

    private static ApplicationContext applicationContext;

    /**
     * 获取spring上下文
     *
     * @param applicationContext 上下文对象，包含了所有被Spring管理的Bean
     * @throws BeansException 获取应用上下文过程中发生错误时抛出
     */
    @Override
    public void setApplicationContext(@NonNull ApplicationContext applicationContext) throws BeansException {
        SQLUtil.applicationContext = applicationContext;
    }

    /**
     * 根据字段查询数据库
     *
     * @param columnName 字段名
     * @param field      字段值
     * @return list集合
     */
    public static <T> List<T> selectToListWhere(SFunction<T, ?> columnName, Object field, Class<T> entityClass) {

        if (field == null) {
            return Collections.emptyList();
        }

        try {
            LambdaQueryWrapper<T> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(columnName, field);

            BaseMapper<T> mapper = getMapper(entityClass);
            return mapper.selectList(queryWrapper);
        } catch (Exception e) {
            return Collections.emptyList();
        }
    }

    /**
     * 从Spring容器中获取Mapper
     *
     * @param entityClass 泛型字节码文件
     * @param <T>         entity
     * @return 对应mapper
     */
    private static <T> BaseMapper<T> getMapper(Class<T> entityClass) {
        String mapperBeanName = getMapperName(entityClass.getName());
        return (BaseMapper<T>) applicationContext.getBean(mapperBeanName);
    }

    /**
     * 拼接得到mapper名字
     *
     * @param path entity变量名
     * @return 对应mapper名
     */
    private static String getMapperName(String path) {
        String mapperName = path.substring(path.lastIndexOf(".") + 1);
        return mapperName.substring(0, 1).toLowerCase() + mapperName.substring(1) + "Mapper";
    }
}
