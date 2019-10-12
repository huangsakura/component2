package org.yui.base.bean.api;

import org.yui.base.annotation.doc.ApiField;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * api 接口返回的json格式
 * @param <T>
 * @author huangjinlong
 */
@Getter
@Setter
public class JsonResult<T> implements Serializable {

    @ApiField(desc = "返回code",example = "SUCCESS")
    private String code;

    @ApiField(desc = "返回message",example = "成功")
    private String message;

    @ApiField(desc = "返回对象")
    private T content;

    public static final String SUCCESS = "SUCCESS";

    public static final String SUCCESS_MESSAGE = "成功";

    public static final String FAIL = "FAIL";

    public static final String FAIL_MESSAGE = "操作失败";

    /**
     * 构造方法
     */
    public JsonResult() {
        this.code = SUCCESS;
        this.message = "成功";
    }
}
