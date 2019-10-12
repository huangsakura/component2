package org.mapper;

import org.apache.ibatis.annotations.InsertProvider;
import org.mapper.provider.InsertListProvider;

import java.util.List;

/**
 * @author huangjinlong
 * @time 2019-07-22 10:06
 * @description
 *
 * 适用于mysql
 */
public interface InsertListMapper<T> {

    /**
     *
     * @param recordList
     * @return
     */
    @InsertProvider(type = InsertListProvider.class, method = "dynamicSQL")
    int insertList(List<T> recordList);
}
