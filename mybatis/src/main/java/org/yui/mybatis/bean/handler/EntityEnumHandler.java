package org.yui.mybatis.bean.handler;

import org.yui.base.enums.entity.EntityEnum;
import org.yui.base.exception.BusinessException;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 处理 把枚举存入数据库，把数据库取出来的文本转为枚举的情况
 * @param <T>
 */
public final class EntityEnumHandler<T extends Enum<T> & EntityEnum> extends BaseTypeHandler<T> {

    private Class<T> type;

    public EntityEnumHandler(Class<T> type) {
        if (null == type) {
            throw new IllegalArgumentException("type参数不能为空");
        }
        this.type = type;
    }


    @Override
    public void setNonNullParameter(PreparedStatement preparedStatement, int i, T t, JdbcType jdbcType) throws SQLException {
        if (jdbcType == null) {
            preparedStatement.setString(i, t.getDbCode());
        } else {
            preparedStatement.setObject(i,t.getDbCode(), jdbcType.TYPE_CODE);
        }
    }

    @Override
    public T getNullableResult(ResultSet resultSet, String s) throws SQLException {
        String value = resultSet.getString(s);
        return value == null ? null : valueOf(type,value);
    }

    @Override
    public T getNullableResult(ResultSet resultSet, int i) throws SQLException {
        String value = resultSet.getString(i);
        return value == null ? null : valueOf(type,value);
    }

    @Override
    public T getNullableResult(CallableStatement callableStatement, int i) throws SQLException {
        String value = callableStatement.getString(i);
        return value == null ? null : valueOf(type,value);
    }

    /**
     * 根据 dbCode 取出对应的枚举
     * @param dbCode
     * @return
     */
    /*
    private static <E extends Enum<E> & EntityEnumIFace> E valueOf(Class<E> type,String dbCode) {
        E e = EntityEnumIFaceUtil.valueOf(type, dbCode);
        if (null == e) {
            throw new IllegalArgumentException(type.getSimpleName() + "枚举中没有"+dbCode);
        }
        return e;
    }
    */
    private static <E extends Enum<E> & EntityEnum> E valueOf(Class<E> enumClass, String dbCode) {
        E[] enumConstants = enumClass.getEnumConstants();
        if (null != enumConstants) {
            for (E e : enumConstants) {
                if (e.getDbCode().equals(dbCode)) {
                    return e;
                }
            }
        }
        throw new BusinessException("ENUM_NOT_EXIST",enumClass.getSimpleName() + "枚举中没有"+dbCode,false);
    }
}
