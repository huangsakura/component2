package org.mapper;

import org.yui.base.bean.entity.Entitys;
import tk.mybatis.mapper.common.Marker;
import tk.mybatis.mapper.common.base.BaseSelectMapper;
import tk.mybatis.mapper.common.base.insert.InsertMapper;
import tk.mybatis.mapper.common.base.insert.InsertSelectiveMapper;
import tk.mybatis.mapper.common.base.update.UpdateByPrimaryKeyMapper;
import tk.mybatis.mapper.common.base.update.UpdateByPrimaryKeySelectiveMapper;
import tk.mybatis.mapper.common.example.SelectByExampleMapper;
import tk.mybatis.mapper.common.example.SelectCountByExampleMapper;
import tk.mybatis.mapper.common.example.UpdateByExampleMapper;
import tk.mybatis.mapper.common.example.UpdateByExampleSelectiveMapper;
import tk.mybatis.mapper.common.rowbounds.SelectByExampleRowBoundsMapper;
import tk.mybatis.mapper.common.rowbounds.SelectRowBoundsMapper;

/**
 * 所有的dao都必须继承这个接口
 * @param <T>
 */
public interface GeneralMapper<T extends Entitys>
        extends BaseSelectMapper<T>, InsertSelectiveMapper<T>, UpdateByPrimaryKeySelectiveMapper<T>,
                SelectByExampleMapper<T>, SelectCountByExampleMapper<T>, UpdateByExampleSelectiveMapper<T>,
                InsertListMapper<T>,Marker, SelectByExampleRowBoundsMapper<T>,
                SelectRowBoundsMapper<T>, InsertMapper<T>, UpdateByPrimaryKeyMapper<T>, UpdateByExampleMapper<T> {
}
