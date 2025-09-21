package com.qg.common.utils;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;

import java.util.Collections;
import java.util.List;

public interface YBaseMapper<T> extends BaseMapper<T> {

    /**
     * 根据字段查询数据库
     *
     * @param columnName 字段名
     * @param field      字段值
     * @return list集合
     */
    default List<T> selectToListWhere(SFunction<T, ?> columnName, Object field) {
        if (field == null) {
            return Collections.emptyList();
        }

        try {
            LambdaQueryWrapper<T> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(columnName, field);
            return this.selectList(queryWrapper);
        } catch (Exception e) {
            return Collections.emptyList();
        }
    }

    /**
     * 批量插入记录
     *
     * @param entities 实体对象集合
     * @return 成功与否
     */
    default boolean insertBatch(List<T> entities) {
        if (entities == null || entities.isEmpty()) {
            return false;
        }

        try {
            for (T entity : entities) {
                if (this.insert(entity) <= 0) {
                    return false;
                }
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 根据字段删除记录
     *
     * @param columnName 字段名
     * @param field      字段值
     * @return 成功与否
     */
    default boolean deleteWhere(SFunction<T, ?> columnName, Object field) {
        if (field == null) {
            return false;
        }

        try {
            LambdaQueryWrapper<T> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(columnName, field);
            return this.delete(queryWrapper) > 0;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 根据条件更新记录
     *
     * @param entity     要更新的字段值
     * @param columnName 条件字段名
     * @param field      条件字段值
     * @return 成功与否
     */
    default boolean updateWhere(SFunction<T, ?> columnName, Object field, T entity) {
        if (entity == null || field == null) {
            return false;
        }

        try {
            LambdaQueryWrapper<T> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(columnName, field);
            return this.update(entity, queryWrapper) > 0;
        } catch (Exception e) {
            return false;
        }
    }
}
