package org.yui.base.bean.api;

import org.yui.base.annotation.doc.ApiField;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

/**
 * @author huangjinlong
 * @time 2019-04-01 15:45
 * @description
 *
 * 如果这个类增加字段，请同步修改
 * com.yunhuakeji.component.base.bean.dto.api.request.BasePageRequest
 */
@Setter
@Getter
public abstract class AbstractPageRequest extends AbstractRequest {

    @ApiField(desc = "分页查询的页码，从1开始计数")
    @NotNull
    private Integer pageNum;


    @ApiField(desc = "分页查询的每页条数")
    @NotNull
    private Integer pageSize;
}
