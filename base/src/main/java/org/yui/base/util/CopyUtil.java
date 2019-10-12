package org.yui.base.util;

import lombok.extern.log4j.Log4j2;
import net.sf.cglib.beans.BeanCopier;
import net.sf.cglib.core.Converter;
import org.apache.commons.beanutils.PropertyUtils;
import org.yui.base.bean.constant.StringConstant;

import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author huangjinlong
 * 拷贝工具类
 */
@Log4j2
public abstract class CopyUtil {

    /**
     *
     *
     */
    private static Map<String,BeanCopier> BEAN_COPIER_MAP;

    /**
     * 属性拷贝
     * 会拷贝null值
     * @param from
     * @param to
     */
    public static void copyProperties(Object from,Object to) {
        copyProperties(from,to,null);
    }

    /**
     * 属性拷贝
     * @param from
     * @param to
     * @param converter
     */
    public static void copyProperties(Object from, Object to, Converter converter) {
        Class<?> fromClass = from.getClass();
        Class<?> toClass = to.getClass();
        String key = generateKey(fromClass,toClass);

        BeanCopier beanCopier = getBeanCopier(key);
        if (null == beanCopier) {
            beanCopier = BeanCopier.create(fromClass,toClass,(converter != null));
            putBeanCopier(key,beanCopier);
        }
        beanCopier.copy(from,to,converter);
    }

    /**
     * 属性拷贝
     * 忽略null
     * @param from
     * @param to
     */
    public static void copyPropertiesIgnoreNull(Object from,Object to) {
        for (PropertyDescriptor propertyDescriptor : PropertyUtils.getPropertyDescriptors(to.getClass())) {
            String key = propertyDescriptor.getName();
            if (BeanUtil.CLASS_STRING.equalsIgnoreCase(key)) {
                continue;
            }

            if (PropertyUtils.isReadable(from, key) && PropertyUtils.isWriteable(to, key)) {
                try {
                    Object value = PropertyUtils.getSimpleProperty(from, key);
                    if (value != null) {
                        PropertyUtils.setSimpleProperty(to, key, value);
                    }
                } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                    log.error("属性拷贝时出错:{}",e.getMessage());
                }
            }
        }
    }

    /**
     *
     * @param key
     * @return
     */
    private static BeanCopier getBeanCopier(String key) {
        singleton0();
        return BEAN_COPIER_MAP.get(key);
    }

    /**
     *
     * @param key
     * @param beanCopier
     */
    private static void putBeanCopier(String key,BeanCopier beanCopier) {
        singleton0();
        BEAN_COPIER_MAP.put(key,beanCopier);
    }

    /**
     *
     * @param from
     * @param to
     * @return
     */
    private static String generateKey(Class<?> from,Class<?> to) {
        return from.getName() + StringConstant.UNDERLINE + to.getName();
    }


    /**
     * 单例模式
     */
    private static void singleton0() {
        if (null == BEAN_COPIER_MAP) {
            synchronized (CopyUtil.class) {
                if (null == BEAN_COPIER_MAP) {
                    BEAN_COPIER_MAP = new ConcurrentHashMap<>();
                }
            }
        }
    }
}
