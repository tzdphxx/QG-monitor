package com.qg.common.utils;


import com.baomidou.mybatisplus.core.toolkit.support.SFunction;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.function.BiPredicate;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * 对传入的list集合进行条件查询
 *
 * @param <T>
 */
public class ConditionQueryListBuilder<T> {

    private List<T> originalList;
    private final List<Predicate<T>> conditions = new ArrayList<>();

    /**
     * 私有化构造方法
     *
     * @param list 初始集合
     */
    private ConditionQueryListBuilder(List<T> list) {
        this.originalList
                = list != null
                ? new ArrayList<>(list)
                : new ArrayList<>();
    }

    /**
     * 开始查询
     *
     * @param list 初始集合
     * @param <T>  结果entity类型
     * @return 建造者
     */
    public static <T> ConditionQueryListBuilder<T> start(List<T> list) {
        return new ConditionQueryListBuilder<>(list);
    }

    /**
     * 条件：等于
     *
     * @param column        从entity中获取某个变量
     * @param expectedValue 期望值
     * @return 建造者本身
     */
    public ConditionQueryListBuilder<T> and(SFunction<T, ?> column, Object expectedValue) {
        return addCondition(column, expectedValue, Objects::equals);
    }

    /**
     * 条件：不等于
     *
     * @param column        从entity中获取某个变量
     * @param expectedValue 期望值
     * @return 建造者本身
     */
    public ConditionQueryListBuilder<T> negate(SFunction<T, ?> column, Object expectedValue) {
        return addCondition(column, expectedValue, (a, b) -> !Objects.equals(a, b));
    }

    /**
     * 条件：模糊查询
     *
     * @param column 从entity中获取某个变量
     * @param value  期望值
     * @return 建造者本身
     */
    public ConditionQueryListBuilder<T> like(SFunction<T, String> column, String value) {
        conditions.add(item -> {
            try {
                String actualValue = column.apply(item);
                return actualValue != null && actualValue.contains(value);
            } catch (Exception e) {
                return false;
            }
        });
        return this;
    }

    /**
     * 添加查询条件
     *
     * @param column        从entity中获取某个变量
     * @param expectedValue 期望值
     * @param comparator    比较方式
     * @return 建造者本身
     */
    private ConditionQueryListBuilder<T> addCondition(SFunction<T, ?> column, Object expectedValue,
                                                      BiPredicate<Object, Object> comparator) {
        conditions.add(item -> {
            try {
                Object actualValue = column.apply(item);
                System.err.println("\n" + "传入了：" + expectedValue + "实际值：" + actualValue);
                return comparator.test(actualValue, expectedValue);
            } catch (Exception e) {
                return false;
            }
        });
        return this;
    }

    /**
     * 执行条件查询
     *
     * @return 查询结果
     */
    public List<T> finish() {

        if (originalList.isEmpty()) {
            return Collections.emptyList();
        }
        if (conditions.isEmpty()) {
            return new ArrayList<>(originalList);
        }

        try {
            return originalList.stream()
                    .filter(Objects::nonNull)
                    .filter(item -> conditions.stream()
                            .allMatch(condition -> condition.test(item)))
                    .collect(Collectors.toList());
        } catch (Exception e) {
            return Collections.emptyList();
        } finally {
            releaseConditions();
            releaseOriginalList();
        }
    }

    /**
     * 清空查询条件
     */
    public void releaseConditions() {
        conditions.clear();
    }

    /**
     * 清空原始集合
     */
    public void releaseOriginalList() {
        originalList.clear();
    }
}
