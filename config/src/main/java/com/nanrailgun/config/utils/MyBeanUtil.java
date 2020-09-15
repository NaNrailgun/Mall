package com.nanrailgun.config.utils;

import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.List;

public class MyBeanUtil {

    /**
     * 复制List
     */
    public static <T> List<T> copyList(List source, Class<T> clazz) {
        List<T> targetList = new ArrayList<>();
        if (source != null) {
            for (Object o : source) {
                try {
                    T target = clazz.newInstance();
                    BeanUtils.copyProperties(o, target);
                    targetList.add(target);
                } catch (InstantiationException | IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
            return targetList;
        }
        return null;
    }
}
