package com.web.util;

import org.springframework.beans.BeanUtils;
import org.springframework.util.ReflectionUtils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ConvertUtils {

    public static Map<String, Object> objectToMap(Object obj) {
        return Arrays.stream(BeanUtils.getPropertyDescriptors(obj.getClass()))
                .filter(pd -> !"class".equals(pd.getName()))
                .collect(HashMap::new,
                        (map, pd) -> map.put(pd.getName(), ReflectionUtils.invokeMethod(pd.getReadMethod(), obj)),
                        HashMap::putAll);
    }

    public static List<String> objectFieldNameToList(Object obj) {
        return Arrays.stream(BeanUtils.getPropertyDescriptors(obj.getClass()))
                .filter(pd -> !"class".equals(pd.getName())).map(pd -> {
                    return pd.getName();
                }).collect(Collectors.toList());
    }
}
