package org.yui.base.util;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang3.StringUtils;
import org.yui.base.constant.StringConstant;
import org.yui.base.enums.Enums;

import javax.annotation.Nullable;
import javax.validation.constraints.NotNull;
import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * @author huangjinlong
 */
@Log4j2
public abstract class MapUtil {

    /**
     * 转换map
     *
     * @param paramMap
     * @return
     */
    @NotNull
    public static Map<String, String> getStringMap(Map<String, String[]> paramMap) {
        if ((paramMap == null) || (paramMap.isEmpty())) {
            return Collections.emptyMap();
        }
        Map<String, String> result = new HashMap<>(32);

        for (Map.Entry<String, String[]> entry : paramMap.entrySet()) {
            String[] value = entry.getValue();
            if (value == null) {
                continue;
            }
            String newValue = StringUtils.join(value, StringConstant.GENERAL_COMMA_SPLIT);
            result.put(entry.getKey(), newValue);
        }
        return result;
    }

    /**
     * 把java类转为map
     * <p>
     * 还有一种方法，就是用常见的反射来实现，后面可以补上
     */
    @Nullable
    public static Map<String, Object> parseBean2Map(Serializable serializable) {

        if (null == serializable) {
            return null;
        }

        Map<String, Object> result = new HashMap<>(32);

        try {
            for (PropertyDescriptor propertyDescriptor : PropertyUtils.getPropertyDescriptors(serializable.getClass())) {
                String key = propertyDescriptor.getName();
                if (BeanUtil.CLASS_STRING.equalsIgnoreCase(key)) {
                    continue;
                }
                Method method = propertyDescriptor.getReadMethod();
                if (null == method) {
                    result.put(key, null);
                } else {
                    result.put(key, method.invoke(serializable));
                }
            }
        } catch (IllegalAccessException | IllegalArgumentException |
                InvocationTargetException e) {
            log.error("反射调用出错:{}",e.getMessage());
            return null;
        }
        return result;
    }

    /**
     * 把map转为java类
     * <p>
     * 目前有问题
     *
     * @param map
     * @param clazz
     * @return
     */
    @Deprecated
    public static Object parseMap2Bean(Map map, Class<?> clazz) {

        if (null == map) {
            return null;
        }

        Object object = null;
        try {
            object = clazz.newInstance();
        } catch (Exception e) {
            return null;
        }

        BeanInfo beanInfo = null;
        try {
            beanInfo = Introspector.getBeanInfo(object.getClass());
        } catch (Exception e) {
            return null;
        }
        PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();

        try {
            for (PropertyDescriptor propertyDescriptor : propertyDescriptors) {

                Method method = propertyDescriptor.getWriteMethod();
                if (null != method) {
                    Object o = map.get(propertyDescriptor.getName());
                    Parameter[] parameters = method.getParameters();

                    /**
                     * 仅考虑method只有一个参数的情况
                     */
                    if (parameters.length == 0) {
                        return null;
                    }
                    Parameter parameter = parameters[0];
                    Class<?> parameterClass = parameter.getType();
                    if (Enums.class.isAssignableFrom(parameterClass)) {
                        method.invoke(object, (Enums) o);
                    } else {
                        method.invoke(object, o);
                    }
                }
            }
        } catch (IllegalAccessException | IllegalArgumentException |
                InvocationTargetException e) {
            return null;
        }
        return object;
    }


    /**
     * 这段代码用于测试
     * <p>
     * 开始--------------------------------------------------
     */

    @Getter
    @Setter
    private static class A implements Serializable {

        private String a1 = "a1";

        private static String a2 = "a2";

        public String a3 = "a3";

        protected String a4 = "a4";
    }

    @Getter
    @Setter
    private static class B extends A {

        private String b1 = "b1";
    }

    public static void main(String[] args) {

        Map<String, Object> map = parseBean2Map(new B());
        System.out.println(map);
    }

    /**
     * ---结束-----------------------------------------------------
     */
}
