package org.mapper.provider;

import com.google.common.collect.Lists;
import org.apache.ibatis.mapping.MappedStatement;
import org.yui.base.bean.constant.StringConstant;
import org.yui.mybatis.util.JdbcUtil;
import tk.mybatis.mapper.entity.EntityColumn;
import tk.mybatis.mapper.mapperhelper.EntityHelper;
import tk.mybatis.mapper.mapperhelper.MapperHelper;
import tk.mybatis.mapper.mapperhelper.MapperTemplate;
import tk.mybatis.mapper.mapperhelper.SqlHelper;

import java.util.List;
import java.util.Set;

/**
 * @author huangjinlong
 * @time 2019-07-22 10:07
 * @description
 */
public class InsertListProvider extends MapperTemplate {

    public InsertListProvider(Class<?> mapperClass, MapperHelper mapperHelper) {
        super(mapperClass, mapperHelper);
    }

    /**
     *
     * @param mappedStatement
     * @return
     */
    public String insertList(MappedStatement mappedStatement) {
        Class<?> entityClass = getEntityClass(mappedStatement);

        switch (JdbcUtil.getDatabaseType()) {
            case MYSQL:{
                return packageMysql(entityClass);
            } case ORACLE:{
                return packageOracle(entityClass);
            } default:{
                throw new IllegalArgumentException("不支持的数据库类型");
            }
        }
    }


    /**
     *
     * @param entityClass
     * @return
     */
    private String packageMysql(Class<?> entityClass) {
        /**
         * 拼接sql
         */
        StringBuilder sql = new StringBuilder();
        sql.append(SqlHelper.insertIntoTable(entityClass, tableName(entityClass)));
        sql.append(SqlHelper.insertColumns(entityClass, false, false, false));
        sql.append(" VALUES ");
        sql.append("<foreach collection=\"list\" item=\"record\" separator=\",\" >");
        sql.append("<trim prefix=\"(\" suffix=\")\" suffixOverrides=\",\">");
        //获取全部列
        Set<EntityColumn> columnList = EntityHelper.getColumns(entityClass);
        //当某个列有主键策略时，不需要考虑他的属性是否为空，因为如果为空，一定会根据主键策略给他生成一个值
        for (EntityColumn entityColumn : columnList) {
            if (entityColumn.isInsertable()) {
                sql.append("<choose><when test=\"record.");
                sql.append(entityColumn.getProperty());
                sql.append(" != null\">");
                sql.append(entityColumn.getColumnHolder("record"));
                sql.append(StringConstant.GENERAL_COMMA_SPLIT);
                sql.append("</when><otherwise>null,</otherwise></choose>");
            }
        }
        sql.append("</trim>");
        sql.append("</foreach>");
        return sql.toString();
    }


    private String packageOracle(Class<?> entityClass) {

        /**
         * 拼接sql
         */
        StringBuilder sql = new StringBuilder();
        sql.append("INSERT ALL ");
        sql.append("<foreach collection=\"list\" item=\"record\" >");
        sql.append("<trim suffixOverrides=\",\">");
        sql.append(" INTO ");
        sql.append(SqlHelper.getDynamicTableName(entityClass, tableName(entityClass)));
        sql.append(" ");
        sql.append(SqlHelper.insertColumns(entityClass, false, false, false));
        sql.append(" VALUES (");
        /**
         * 获取全部列
         */
        List<EntityColumn> columnList = Lists.newArrayList(EntityHelper.getColumns(entityClass));
        /**
         *
         */
        int siz = columnList.size();
        for (int i = 0;i < siz;i ++) {
            EntityColumn entityColumn = columnList.get(i);
            if (entityColumn.isInsertable()) {
                /**
                 * 最后一列
                 */
                boolean lastColumn = (i == (siz-1));

                sql.append("<choose><when test=\"record.");
                sql.append(entityColumn.getProperty());
                sql.append(" != null\">");
                sql.append(entityColumn.getColumnHolder("record"));
                if (!lastColumn) {
                    sql.append(StringConstant.GENERAL_COMMA_SPLIT);
                }
                sql.append("</when><otherwise>null");
                if (!lastColumn) {
                    sql.append(StringConstant.GENERAL_COMMA_SPLIT);
                }
                sql.append("</otherwise></choose>");
            }
        }
        sql.append(")</trim></foreach>");
        sql.append(" SELECT 1 FROM DUAL");
        return sql.toString();
    }
}
