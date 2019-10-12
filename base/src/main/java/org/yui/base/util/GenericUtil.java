package org.yui.base.util;

import javax.annotation.Nullable;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Optional;

/**
 * 泛型工具类
 */
public abstract class GenericUtil {

    public static Class getGenericClass(Class clazz) {
        return getGenericClass(clazz, 0);
    }

    public static Class[] getGenericClasses(Class clazz) {

        /**
         * 返回直接继承的父类，包含有泛型信息
         */
        Type genType = clazz.getGenericSuperclass();
        if (genType instanceof ParameterizedType) {
            /**
             * 进入到这个if条件，说明genType 是含有泛型参数的。
             * getActualTypeArguments() 方法返回 genType 的泛型类型数组
             */
            Type[] params = ((ParameterizedType)genType).getActualTypeArguments();
            /**
             * 返回泛型数组里面 index为 (index-1)的类型
             */
            if (params != null) {
                Class[] classes = new Class[params.length];
                for (int i = 0;i < params.length;i ++) {
                    classes[i] = (Class)params[i];
                }
                return classes;
            }
        }
        return null;
    }

    /**
     * 获取 clazz 的直接父类的泛型参数的第 index 个的类，index从0开始计数
     * @param clazz
     * @param index
     * @return
     */
    @Nullable
    public static Class getGenericClass(Class clazz, int index) {
        return Optional.ofNullable(getGenericClasses(clazz))
                .map(value0 -> {
                    if (index < value0.length) {
                        return value0[index];
                    } else {
                        return null;
                    }
                }).orElse(null);
    }

    /*
    static class A<T,E> {

    }

    static class B extends A<ApiInfo, ApiInfoLog> {

    }

    public static void main(String[] args) {
         //B b = new B();
         System.out.println(getGenericClass(B.class,1));
    }
    */

    /**
     * 获取直接父类的第一个泛型参数
     * @param clazz
     * @return
     */
    public static Class getSuperClassGenericType(final Class clazz) {
        return getSuperClassGenericType(clazz, 0);
    }

    /**
     * 获取 clazz 的直接父类的第index个泛型类，index从0开始计数
     * @param clazz
     * @param index
     * @return
     * @throws IndexOutOfBoundsException
     */
    public static Class getSuperClassGenericType(final Class clazz, final int index) throws IndexOutOfBoundsException {
        if (index < 0) {
            return Object.class;
        }
        final Type genType = clazz.getGenericSuperclass();
        if (!(genType instanceof ParameterizedType)) {
            return Object.class;
        }
        final Type[] params = ((ParameterizedType)genType).getActualTypeArguments();
        if (params.length < (index + 1)) {
            return Object.class;
        }
        if (!(params[index] instanceof Class)) {
            /**
             * 这个if是干什么的？
             */
            return Object.class;
        }
        return (Class)params[index];
    }


    /**
     * 获取一个类的直接父类的泛型
     * @param clazz
     * @return
     */
    @Nullable
    public static Type[] getSuperClassGenericTypes(final Class clazz) {
        final Type genType = clazz.getGenericSuperclass();
        if (!(genType instanceof ParameterizedType)) {
            return null;
        }
        return ((ParameterizedType)genType).getActualTypeArguments();
    }
}
