package org.yui.mybatis.bean.handler;

import org.yui.base.bean.BinaryField;
import org.yui.base.enums.BinaryEnum;
import org.yui.base.exception.BusinessException;
import org.yui.base.util.BeanUtil;
import org.yui.base.util.BinaryUtil;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.Validate;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.springframework.util.CollectionUtils;

import javax.annotation.Nullable;
import javax.validation.constraints.NotNull;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * 二进制typehandler
 *
 * 参见 com.yunhuakeji.component.base.enums.iface.BinaryEnumIFace
 */
@Log4j2
public final class BinaryFieldHandler extends BaseTypeHandler<BinaryField> {

    private Class<? extends BinaryField> sonClass;

    public BinaryFieldHandler(Class<? extends BinaryField> sonClass) {
        this.sonClass = sonClass;
    }

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, BinaryField parameter, JdbcType jdbcType) throws SQLException {

        Set<?> set = parameter.getBinaryEnum();
        int result = 0;
        if (!CollectionUtils.isEmpty(set)) {
            for (Object object : set) {
                Validate.notNull(object,"枚举不能为空");
                BinaryEnum binaryEnum = (BinaryEnum)object;
                result += binaryEnum.getBinaryNumber();
            }
        }
        ps.setInt(i,result);
    }

    @Override
    public BinaryField getNullableResult(ResultSet rs, String columnName) throws SQLException {

        BinaryField binaryField = newAbstractBinaryField();

        int value = rs.getInt(columnName);
        setBinaryEnumListValue(binaryField,value);
        return binaryField;
    }

    @Override
    public BinaryField getNullableResult(ResultSet rs, int columnIndex) throws SQLException {

        BinaryField binaryField = newAbstractBinaryField();

        int value = rs.getInt(columnIndex);
        setBinaryEnumListValue(binaryField,value);
        return binaryField;
    }

    @Override
    public BinaryField getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {

        BinaryField binaryField = newAbstractBinaryField();

        int value = cs.getInt(columnIndex);
        setBinaryEnumListValue(binaryField,value);
        return binaryField;
    }

    @Nullable
    private BinaryField newAbstractBinaryFieldQuitely() {
        try {
            Object object = sonClass.newInstance();
            return  (BinaryField)object;
        } catch (InstantiationException | IllegalAccessException e) {
            log.error("创建实体类二进制对象失败:{}",e.getMessage());
            return null;
        }
    }

    private BinaryField newAbstractBinaryField() {
        Optional<BinaryField> abstractBinaryFieldOptional =
                Optional.ofNullable(newAbstractBinaryFieldQuitely());

        return abstractBinaryFieldOptional.orElseThrow(
                () -> new BusinessException("CREATE_OBJECT_FAIL","创建实体类二进制对象失败",false));
    }

    private void setBinaryEnumListValue(@NotNull BinaryField binaryField, int value) {
        Set<BinaryEnum> result = new HashSet<>();

        List<Integer> integerList = BinaryUtil.separateDecimal2BinaryList(value);

        Method method = BeanUtil.getMethodQuietly(sonClass,BinaryField.GET_BINARY_ENUM);
        if (null != method) {
            /**
             * 获取泛型类型
             */
            Type[] types = ((ParameterizedType)method.getGenericReturnType()).getActualTypeArguments();
            Object[] objects = ((Class<?>)types[0]).getEnumConstants();
            if (null != objects) {
                for (Object object : objects) {
                    BinaryEnum binaryEnum = (BinaryEnum)object;
                    if (integerList.contains(binaryEnum.getBinaryNumber())) {
                        result.add(binaryEnum);
                    }
                }
            }
        }
        binaryField.setBinaryEnum(result);
    }
}
