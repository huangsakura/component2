package org.yui.base.util;

import org.hibernate.validator.constraints.NotBlank;
import org.yui.base.bean.entity.Entitys;

import javax.annotation.Nullable;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 实体工具类
 */
public abstract class EntityUtil {

    /**
     * 实体类的主键的redis key的前缀
     */
    private static final String ENTITY_SEQUENCE_REDIS_PREFIX = "SEQ_";
    /**
     * 实体类的code的redis key的前缀
     */
    private static final String ENTITY_CODE_REDIS_PREFIX = "CODE_";

    /**
     * 获取实体类列表的第一个对象
     * @param tList
     * @param <T>
     * @return
     */
    @Nullable
    public static <T extends Entitys> T getFirst(List<T> tList) {

        if (tList.isEmpty()) {
            return null;
        }
        return tList.get(0);
    }

    /**
     * 获取实体类的主键的redis key
     * @param entityClazz
     * @return
     */
    @NotBlank
    public static <T extends Entitys> String getIdRedisKey(@NotNull Class<T> entityClazz) {
        return ENTITY_SEQUENCE_REDIS_PREFIX + entityClazz.getSimpleName();
    }

    /**
     * 获取实体类的code的redis key
     * @param entityClazz
     * @return
     */
    @NotBlank
    public static <T extends Entitys> String getCodeRedisKey(@NotNull Class<T> entityClazz) {
        return ENTITY_CODE_REDIS_PREFIX + entityClazz.getSimpleName();
    }
}
