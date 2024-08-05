package io.dynamic.threadpool.config.toolkit;

import cn.hutool.core.collection.CollUtil;
import com.google.common.collect.Lists;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.BiFunction;

/**
 * Map Util.
 */
public class MapUtil {

    /**
     * 如果map中的key不存在，则调用function函数并传参param1和param2，结果值存入map中
     */
    public static Object computeIfAbsent(Map target, Object key, BiFunction mappingFunction, Object param1, Object param2) {
        Objects.requireNonNull(target, "target");
        Objects.requireNonNull(key, "key");
        Objects.requireNonNull(mappingFunction, "mappingFunction");
        Objects.requireNonNull(param1, "param1");
        Objects.requireNonNull(param2, "param2");

        Object val = target.get(key);
        if (val == null) {
            Object ret = mappingFunction.apply(param1, param2);
            target.put(key, ret);
            return ret;
        }
        return val;
    }

    /**
     * 根据 Key 进行模糊匹配.
     *
     * @param sourceMap
     * @param filters
     * @return
     */
    public static List<String> parseMapForFilter(Map<String, ?> sourceMap, String filters) {
        List<String> resultList = Lists.newArrayList();
        if (CollUtil.isEmpty(sourceMap)) {
            return resultList;
        }

        sourceMap.forEach((key, val) -> {
            if (key.indexOf(filters) > -1) {
                resultList.add(key);
            }
        });

        return resultList;
    }

}
