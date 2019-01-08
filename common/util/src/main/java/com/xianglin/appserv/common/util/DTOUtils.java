package com.xianglin.appserv.common.util;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * 对象COPY
 *
 * @class com.xianglin.xlnodecore.common.util.DTOUtils
 * @date 2015年9月17日 下午2:18:46
 */
public class DTOUtils {

    public static final Logger log = LoggerFactory.getLogger(DTOUtils.class);

    public static <S, T> T map(S source, T dest) throws Exception {
        try {
            if (source == null) {
                return null;
            }
            if (source instanceof Map) {
                BeanUtils.copyProperties(dest, source);
            } else {
                org.springframework.beans.BeanUtils.copyProperties(source, dest);
            }
        } catch (Exception e) {
            throw new RuntimeException("对象转换异常",e);
        }
        return dest;
    }

    public static <S, T> T map(S source, Class<T> targetClass)  {
        T cl = null;
        try {
            if (source == null) {
                return null;
            }
            cl = targetClass.newInstance();
            if (source instanceof Map) {
                BeanUtils.copyProperties(cl, source);
            } else {
                org.springframework.beans.BeanUtils.copyProperties(source, cl);
            }
        } catch (Exception e) {
            throw new RuntimeException("对象转换异常",e);
        }
        return cl;
    }

    public static <S, T> List<T> map(List<S> source, Class<T> targetClass) {
        List<T> list = null;
        if (CollectionUtils.isNotEmpty(source)) {
            list = new ArrayList<>(source.size());
            for (Object obj : source) {
                T target = map(obj, targetClass);
                list.add(target);
            }
        } else {
            list = new ArrayList<>(0);
        }
        return list;
    }

    public static Map<String, Object> beanToMap(Object source) throws Exception{
        return PropertyUtils.describe(source);

    }

    public static Optional<Map<String, Object>> objectToMap(Object source) {
        try {
            return Optional.ofNullable(PropertyUtils.describe(source));
        }catch (Exception e){
            log.warn("",e);
            return Optional.empty();
        }
    }

    public static Map<String, Object> queryMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("startPage", 1);
        map.put("pageSize", 10);
        return map;
    }
}
