package com.shangan.util;

import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author Alva
 * @CreateTime 2021/1/28 15:18
 */
public class BeanUtil {

    /**
     * List 集合之间的对象属性赋值
     * @param sources 源 List 集合
     * @param clazz 目标集合类型
     * @param <T> 目标集合类型
     * @return 目标集合
     */
    public static <T> List<T> copyList(List<?> sources, Class<T> clazz) {
        List<T> targetList = new ArrayList<>();
        if (sources != null) {
            try {
                for (Object source : sources) {
                    T target = clazz.newInstance();
                    BeanUtils.copyProperties(source, target);
                    targetList.add(target);
                }
            } catch (InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return targetList;
    }
}
