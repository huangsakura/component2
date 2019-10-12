package org.yui.base.bean.api;

import org.yui.base.annotation.doc.ApiField;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

/**
 * 分页的response
 * @param <T>
 *
 * @author huangjinlong
 */
@Getter
@Setter
public abstract class PageResponse<T extends Serializable> extends BaseResponse {

    public PageResponse() {}

    public PageResponse(int pageNum,int pageSize,long total,List<T> list) {
        this.list = list;
        this.pageNum = pageNum;
        this.pageSize = pageSize;
        this.total = total;
    }

    public PageResponse(int pageNum,int pageSize,long total) {
        this.pageNum = pageNum;
        this.pageSize = pageSize;
        this.total = total;
    }

    /**
     * 当前页码数
     */
    @ApiField(desc = "当前页码数，从1开始计数",example = "1")
    private Integer pageNum;

    /**
     * 每一页的大小
     */
    @ApiField(desc = "每一页的条数",example = "10")
    private Integer pageSize;

    /**
     *  总条数
     */
    @ApiField(desc = "记录总条数",example = "123")
    private Long total;

    /**
     * 当前页 具体的数据
     */
    @ApiField(desc = "当前页的具体数据")
    private List<T> list;
}
