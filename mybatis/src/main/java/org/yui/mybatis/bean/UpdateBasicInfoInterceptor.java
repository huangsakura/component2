package org.yui.mybatis.bean;

import org.yui.base.annotation.entity.Code;
import org.yui.base.util.BeanUtil;
import org.yui.base.util.DateUtil;
import org.yui.base.util.EntityUtil;
import org.yui.base.util.RandomUtil;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.Validate;
import org.apache.ibatis.binding.MapperMethod;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.session.defaults.DefaultSqlSession;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.yui.base.bean.constant.StringConstant;
import org.yui.base.bean.entity.AbstractBaseEntity;
import org.yui.base.bean.entity.Entitys;
import org.yui.base.enums.entity.StateEnum;

import javax.annotation.Nullable;
import javax.persistence.Id;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author huangjinlong
 * 增改 数据库，mybatis都认为是 update，即mybatis没有专门insert类型
 *
 *  对于insert，则自动赋值 主键，创建时间，状态，最后一次更新时间,大学id，操作人id
 *  对于update,则自动赋值 最后一次更新时间，操作人id
 */
@Component
@Log4j2
@Intercepts({@Signature(type = Executor.class, method = "update", args = {MappedStatement.class, Object.class})})
public class UpdateBasicInfoInterceptor implements Interceptor {

    public static final String UPDATE = "update";

    public static final String MAPPER_CONST_RECORD = "record";
    public static final String MAPPER_CONST_PARAM1 = "param1";

    /**
     *
     */
    private static final String SET_CREATE_TIME_METHOD_NAME = "setCreateTime";
    private static final String SET_STATE_METHOD_NAME = "setState";
    private static final String SET_UPDATE_TIME_METHOD_NAME = "setUpdateTime";

    /**
     * 维护 类名+方法名+参数类型 和 方法 的关系
     */
    private Map<String,Method> entityMethodMap = new ConcurrentHashMap<>(64);

    /**
     * 从 ENTITY_METHOD_MAP 中取出实体类的方法
     * @param clazz
     * @param methodName
     * @param parameterTypes
     * @return
     */
    @Nullable
    private Method getMethodFromMap(Class<?> clazz,String methodName,Class<?>... parameterTypes) {
        String key = packageKey(clazz,methodName,parameterTypes);

        Method[] methods = {null};

        return Optional.ofNullable(entityMethodMap.get(key)).orElseGet(() -> {
            Optional.ofNullable(BeanUtil.getMethodQuietly(clazz,methodName,parameterTypes))
                    .ifPresent((method -> {
                        methods[0] = method;
                        entityMethodMap.put(key,method);
                    }));
            return methods[0];
        });
    }

    /**
     * 组装 ENTITY_METHOD_MAP 的key
     * @param clazz
     * @param methodName
     * @param parameterTypes
     * @return
     */
    private static String packageKey(Class<?> clazz,String methodName,Class<?>... parameterTypes) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(clazz.getName());
        stringBuilder.append(StringConstant.UNDERLINE);
        stringBuilder.append(methodName);
        if (null != parameterTypes) {
            for (Class<?> parameterType : parameterTypes) {
                stringBuilder.append(parameterType.getSimpleName());
                stringBuilder.append(StringConstant.UNDERLINE);
            }
        }
        return stringBuilder.toString();
    }

    @Autowired
    private RedissonClient redissonClient;

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        /**
         * 获取第一个参数
         */
        MappedStatement mappedStatement = (MappedStatement) invocation.getArgs()[0];

        /**
         * 获取方法名
         */
        String methodName = invocation.getMethod().getName();
        /**
         * 获取sql执行类型
         */
        SqlCommandType sqlCommandType = mappedStatement.getSqlCommandType();
        if (UPDATE.equals(methodName)) {

            /**
             * 当前时间
             */
            LocalDateTime now = DateUtil.currentDateTime();
            /**
             * 获取第二个参数
             */
            Object object = invocation.getArgs()[1];
            if (null != object) {

                switch (sqlCommandType) {
                    case INSERT:{
                        if (DefaultSqlSession.StrictMap.class.isAssignableFrom(object.getClass())) {
                            /**
                             * 说明调用的是insertList方法
                             */
                            DefaultSqlSession.StrictMap strictMap = (DefaultSqlSession.StrictMap)object;

                            Object arrayListObject = strictMap.get("list");
                            /**
                             * 实际上，listObject对象的类型是java.util.Arrays.ArrayList，
                             * 但由于java.util.Arrays.ArrayList类是私有静态内部类，
                             * 所以只能通过反射调用这个类的方法；
                             */
                            Method method = Optional.of(
                                    BeanUtil.getMethodQuietly(arrayListObject.getClass(),"toArray"))
                                    .get();
                            Object object1 = BeanUtil.invokeMethodQuietly(method,arrayListObject);
                            Validate.isTrue(object1 instanceof Object[]);

                            for (Object object2 : (Object[])object1) {
                                if (object2 instanceof Entitys) {
                                    updateEntityWhenInsert(object2,now);
                                }
                            }
                        } else {
                            if (object instanceof Entitys) {
                                updateEntityWhenInsert(object,now);
                            }
                        }
                        break;
                    } case UPDATE:{

                        /**
                         * 设置 最后一次更新时间
                         */
                        if (object instanceof Entitys) {

                            /**
                             * 当使用updateByPrimaryKeySelective方法时
                             */
                            updateEntityWhenUpdate(object,now);
                        } else if (object instanceof MapperMethod.ParamMap) {

                            /**
                             * 当调用mapper的updateByExampleSelective方法时，会触发这个else if
                             */
                            MapperMethod.ParamMap paramMap = (MapperMethod.ParamMap)object;

                            paramMap.forEach((key,value) ->  {
                                if (value instanceof AbstractBaseEntity) {
                                    if (MAPPER_CONST_RECORD.equals(key) || MAPPER_CONST_PARAM1.equals(key)) {
                                        updateEntityWhenUpdate(value,now);
                                    }
                                }
                            });
                        }
                        break;
                    } default:{
                        break;
                    }
                }
            }
        } else {
            log.warn("UpdateBasicInfoInterceptor中出现了非update的方法名:{}",methodName);
        }
        Object result = invocation.proceed();
        log.debug("result的类型:{}",result.getClass().getName());
        return result;
    }

    @Override
    public Object plugin(Object o) {
        return Plugin.wrap(o, this);
    }

    @Override
    public void setProperties(Properties properties) {}

    /**
     *
     * @param value
     * @param now
     */
    private void updateEntityWhenUpdate(Object value,LocalDateTime now) {

        Method methodStateDate = getMethodFromMap(value.getClass(),SET_UPDATE_TIME_METHOD_NAME,LocalDateTime.class);
        if (null != methodStateDate) {
            BeanUtil.invokeMethodQuietly(methodStateDate,value, now);
        }
    }

    /**
     *
     * @param object
     * @param localDateTime
     * @return
     * @throws Throwable
     */
    private void updateEntityWhenInsert(Object object,LocalDateTime localDateTime) throws Throwable {

        Class clazz = object.getClass();

        /**
         * 是否找到主键字段和code
         * 索引0表示主键
         * 索引1表示code
         */
        boolean[] findIdAndCode = {false,false};

        for (Field field : clazz.getDeclaredFields()) {
            /**
             * 默认一张表只有一个id，不考虑联合主键的情况
             */
            if (!findIdAndCode[0]) {
                Optional<Id> idAnnotationOptional = Optional.ofNullable(field.getAnnotation(Id.class));
                idAnnotationOptional.ifPresent((idAnnotation) -> {
                    /**
                     * 主键
                     */
                    Object id = BeanUtil.getFieldValueQuietly(field,object);
                    if (id == null) {
                        /**
                         * redis key
                         */
                        String redisKey = EntityUtil.getIdRedisKey(clazz);
                        id = String.valueOf(System.currentTimeMillis()) +
                                redissonClient.getAtomicLong(redisKey).incrementAndGet();
                        BeanUtil.setFieldValueQuietly(field,object,id);
                    }
                    findIdAndCode[0] = true;
                });
            }
            if (!findIdAndCode[1]) {
                Optional<Code> codeAnnotationOptional = Optional.ofNullable(field.getAnnotation(Code.class));
                codeAnnotationOptional.ifPresent((codeAnnotation) -> {
                    /**
                     * code 字段
                     */
                    Object code = BeanUtil.getFieldValueQuietly(field,object);
                    if (code == null) {
                        /**
                         * redis key
                         */
                        String redisKey = EntityUtil.getCodeRedisKey(clazz);
                        code = RandomUtil.randomCode(8) +
                                redissonClient.getAtomicLong(redisKey).incrementAndGet();
                        BeanUtil.setFieldValueQuietly(field,object,code);
                    }
                    findIdAndCode[1] = true;
                });
            }
            if (findIdAndCode[0] && findIdAndCode[1]) {
                break;
            }
        }
        /**
         * 设置 创建时间，状态，最后一次更新时间
         */
        Method methodCreateTime = getMethodFromMap(clazz,SET_CREATE_TIME_METHOD_NAME,LocalDateTime.class);
        if (null != methodCreateTime) {
            methodCreateTime.invoke(object, localDateTime);
        }

        Method methodState = getMethodFromMap(clazz,SET_STATE_METHOD_NAME, StateEnum.class);
        if (null != methodState) {
            methodState.invoke(object, StateEnum.VALID);
        }

        Method methodUpdateTime = getMethodFromMap(clazz,SET_UPDATE_TIME_METHOD_NAME,LocalDateTime.class);
        if (null != methodUpdateTime) {
            methodUpdateTime.invoke(object, localDateTime);
        }
    }
}
