package org.yui.mybatis.bean.handler;

import org.yui.base.util.DateUtil;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.MonthDay;

/**
 * 要求，数据库相关字段的类型是 char(5)
 *
 * MonthDay的typehandler
 */
public final class MonthDayHandler extends BaseTypeHandler<MonthDay> {

    /**
     * 静态
     */
    public static final MonthDayHandler INSTANCE = new MonthDayHandler();

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, MonthDay parameter, JdbcType jdbcType) throws SQLException {
        if (null != parameter) {
            ps.setString(i, DateUtil.format(parameter, DateUtil.DateTimeFormatEnum.DATE_TIME_FORMAT_13));
        } else {
            ps.setObject(i,null);
        }
    }

    @Override
    public MonthDay getNullableResult(ResultSet rs, String columnName) throws SQLException {
        String value = rs.getString(columnName);
        return value == null ? null : MonthDay.parse(value,DateUtil.DATE_TIME_FORMATTER_13);
    }

    @Override
    public MonthDay getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        String value = rs.getString(columnIndex);
        return value == null ? null : MonthDay.parse(value,DateUtil.DATE_TIME_FORMATTER_13);
    }

    @Override
    public MonthDay getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        String value = cs.getString(columnIndex);
        return value == null ? null : MonthDay.parse(value,DateUtil.DATE_TIME_FORMATTER_13);
    }
}
