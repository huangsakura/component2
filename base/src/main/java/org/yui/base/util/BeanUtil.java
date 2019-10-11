package org.yui.base.util;

import lombok.extern.log4j.Log4j2;
import org.hibernate.validator.constraints.NotBlank;

import javax.annotation.Nullable;
import javax.validation.constraints.NotNull;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * 利用java反射做的一些工具类
 */

/**
 * @author huangjinlong
 */
@Log4j2
public abstract class BeanUtil {

    /**
     * 常量class
     */
    static final String CLASS_STRING = "class";

    /**
     * 从 object 里面取出 所有 类型为type的（共有/私有/受保护，但不包括父类）域
     * @param object
     * @param type
     * @return
     */
    public static List<Field> getFieldsByType(final Object object, final Class type) {
        final List<Field> list = new ArrayList<>();
        final Field[] declaredFields = object.getClass().getDeclaredFields();
        for (final Field field : declaredFields) {
            if (field.getType().isAssignableFrom(type)) {
                list.add(field);
            }
        }
        return list;
    }

    /**
     * 从 object 中取出 域名称propertyName  的值
     * @param object
     * @param propertyName
     * @return
     * @throws IllegalAccessException
     * @throws NoSuchFieldException
     */
    public static Object getDeclaredProperty(@NotNull Object object, @NotBlank String propertyName) throws IllegalAccessException, NoSuchFieldException {
        final Field field = getDeclaredField(object, propertyName);
        return getDeclaredProperty(object, field);
    }

    /**
     * 从 object 中取出 域field 的值
     * @param object
     * @param field
     * @return
     * @throws IllegalAccessException
     */
    public static Object getDeclaredProperty(@NotNull Object object, @NotNull Field field) throws IllegalAccessException {
        /**
         * 先取出域field是否可访问？
         */
        final boolean accessible = field.isAccessible();
        /**
         * 把该域设置为可访问
         */
        field.setAccessible(true);
        final Object result = field.get(object);
        /**
         * 然后再把这个域恢复原值
         * 为什么要这么写？难道这里会改变域原始的可访问性？----位置A
         *
         * 经过测试，真的会这样
         */
        field.setAccessible(accessible);
        return result;
    }

    /**
     * 给 field 赋值
     * @param field
     * @param object
     * @param value
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     */
    public static void setFieldValue(Field field,Object object,Object value)
            throws IllegalArgumentException,IllegalAccessException {
        /**
         * 先将这个field本来的可访问性保存下来
         */
        boolean accessible = field.isAccessible();
        field.setAccessible(true);
        try {
            field.set(object,value);
        } finally {
            field.setAccessible(accessible);
        }
    }

    /**
     * 给域赋值，吞掉异常
     * @param field
     * @param object
     * @param value
     * @return
     */
    public static boolean setFieldValueQuietly(Field field,Object object,Object value) {
        try {
            setFieldValue(field,object,value);
            return true;
        } catch (IllegalArgumentException | IllegalAccessException e) {
            log.error("设置对象的域值失败:{}",e.getMessage());
            return false;
        }
    }


    /**
     * 取 field的值
     * @param field
     * @param object
     * @return
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     */
    public static Object getFieldValue(Field field,Object object)
            throws IllegalArgumentException,IllegalAccessException {
        /**
         * 先将这个field本来的可访问性保存下来
         */
        boolean accessible = field.isAccessible();
        field.setAccessible(true);
        try {
            return field.get(object);
        } finally {
            field.setAccessible(accessible);
        }
    }

    /**
     * 取域的值，吞掉异常
     * @param field
     * @param object
     * @return
     */
    public static Object getFieldValueQuietly(Field field,Object object) {
        try {
            return getFieldValue(field,object);
        } catch (IllegalArgumentException | IllegalAccessException e) {
            log.error("获取对象的域值失败:{}",e.getMessage());
            return null;
        }
    }


    public static void main(String[] args) throws NoSuchFieldException,IllegalAccessException {


        /*
        * 位置A的测试程序
        ApiInfo apiInfo = new ApiInfo();
        apiInfo.setApiId(100L);
        Field field = ApiInfo.class.getDeclaredField("apiId");
        System.out.println(getDeclaredProperty(apiInfo,field));
        field.set(apiInfo,200L);
        */
    }

    /**
     * 从对象 object 取出 属性名为propertyName 的域
     * @param object
     * @param propertyName
     * @return
     * @throws NoSuchFieldException
     */
    public static Field getDeclaredField(@NotNull Object object, @NotBlank String propertyName) throws NoSuchFieldException {
        return getDeclaredField(object.getClass(), propertyName);
    }

    /**
     * 从类 clazz 取出 属性名为propertyName 的域
     * @param clazz
     * @param propertyName
     * @return
     * @throws NoSuchFieldException
     */
    public static Field getDeclaredField(@NotNull Class clazz, @NotBlank String propertyName) throws NoSuchFieldException {
        Class superClass = clazz;
        /**
         * 一层一层往上面找，直到父类是Object类
         */
        while (superClass != Object.class) {
            try {
                return superClass.getDeclaredField(propertyName);
            }
            catch (NoSuchFieldException ex) {
                superClass = superClass.getSuperclass();
            }
        }
        throw new NoSuchFieldException(clazz.getName() + "类及其父类中没有名为" + propertyName + "的域");
    }


    /**
     * 吞掉异常
     * @param clazz
     * @param propertyName
     * @return
     */
    @Nullable
    public static Field getDeclaredFieldQuietly(@NotNull Class clazz, @NotBlank String propertyName) {
        try {
            return getDeclaredField(clazz,propertyName);
        } catch (NoSuchFieldException e) {
            return null;
        }
    }

    /*
    public static <T,E> PageInfo<T> copyPageInfo(PageInfo<E> pageInfo) {
        PageInfo<T> tPageInfo = new PageInfo<>();
        BeanCopier copier = BeanCopier.create(PageInfo.class, PageInfo.class, false);
        copier.copy(pageInfo, tPageInfo, null);
        return tPageInfo;
    }
    */

    /**
     * 在 当前类及其父类的所有的公有方法中选择 方法名和参数匹配的方法
     * @param clazz
     * @param methodName
     * @param parameterTypes
     * @return
     * @throws NoSuchMethodException
     * @throws SecurityException
     */
    public static Method getMethod(final Class clazz,final String methodName,final Class<?>... parameterTypes)
        throws NoSuchMethodException,SecurityException {
        return clazz.getMethod(methodName,parameterTypes);
    }

    /**
     * 在 当前类及其父类的所有的公有方法中选择 方法名和参数匹配的方法
     * 并吞掉异常
     * @param clazz
     * @param methodName
     * @param parameterTypes
     * @return
     */
    @Nullable
    public static Method getMethodQuietly(final Class clazz,final String methodName,final Class<?>... parameterTypes) {
        try {
            return getMethod(clazz,methodName,parameterTypes);
        } catch (NoSuchMethodException | SecurityException e) {
            return null;
        }
    }

    /**
     * 调用方法
     * @param method
     * @param object
     * @param args
     * @return
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     * @throws InvocationTargetException
     */
    public static Object invokeMethod(Method method, Object object,Object... args)
            throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        return method.invoke(object,args);
    }


    /**
     * 调用方法，并吞掉异常
     * @param method
     * @param object
     * @param args
     * @return
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     * @throws InvocationTargetException
     */
    @Nullable
    public static Object invokeMethodQuietly(Method method, Object object,Object... args) {
        try {
            boolean accessible = method.isAccessible();
            method.setAccessible(true);
            Object result = method.invoke(object,args);
            method.setAccessible(accessible);
            return result;
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
            log.error("调用对象的方法失败:{}",e.getMessage());
            return null;
        }
    }
}
